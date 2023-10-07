package com.example.threadsapp.view.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentSomeoneProfileBinding
import com.example.threadsapp.view.home.ReplyFragmentArgs
import com.example.threadsapp.viewModel.followViewModel.FollowSomeoneViewModel
import com.example.threadsapp.viewModel.followViewModel.MutualFollowViewModel
import com.example.threadsapp.viewModel.followViewModel.UnfollowViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class SomeoneProfileFragment : Fragment() {
    private lateinit var binding: FragmentSomeoneProfileBinding
    private val viewModel: SomeoneProfileViewModel by viewModels()
    private val followViewModel: FollowSomeoneViewModel by viewModels()
    private val unfollowViewModel: UnfollowViewModel by viewModels()
    private val mutualFollowViewModel: MutualFollowViewModel by viewModels()
    private var userProfileId: Int = -1
    private var followingStatus: String? = null
    private val args: SomeoneProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSomeoneProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userProfileId = args.id
        checkFollow(args.id)
        setupNavigation()
        setupDataByID(userProfileId)
    }

    private fun setupNavigation() {
        binding.meetballBtn.setOnClickListener {
            showDialog()
        }
        binding.btnFollow.setOnClickListener {
            followBtn(userProfileId)
        }
    }

    private fun setupDataByID(id: Int) {
        viewModel.getUserProfile(
            id.toString(),
            onSuccess = { userProfile ->
                userProfileId = userProfile.pk
                userProfile.photo.let { photoUrl ->
                    if (photoUrl.isNullOrEmpty()) {
                        Glide.with(binding.root.context).load(R.drawable.profile_photo).into(binding.profilePhoto)
                    } else {
                        Glide.with(binding.root.context).load(photoUrl).circleCrop().into(binding.profilePhoto)
                    }
                }
                binding.username.text = userProfile.username
                binding.name.text = userProfile.full_name
            },
            onError = { Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show() }
        )
    }

    private fun checkFollow(id: Int) {
        mutualFollowViewModel.mutualFollow(
            id,
            onSuccess = {
                viewModel.getUserProfileById(
                    id,
                    onSuccess = { result ->
                        val initialText = if(result.is_followed == "Followed") "Following" else "Follow"
                        updateFollowButtonState(initialText)
                    },
                    onError = {
                        Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onError = {
                Toast.makeText(requireContext(), "Try again. Failed to check following status", Toast.LENGTH_SHORT).show()
            })
    }

    @SuppressLint("ResourceAsColor")
    private fun followBtn(id: Int) {
        if (followingStatus == "Followed") {
            unfollow(id)
        } else {
            follow(id)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun follow(id: Int) {
        followViewModel.follow(
            id,
            onSuccess =  {
                followingStatus = "Followed"
                updateFollowButtonState("Following")
            },
            onError = {
                Toast.makeText(requireContext(), "try again", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun unfollow(id: Int) {
        unfollowViewModel.unfollow(
            id,
            onSuccess =  {
                followingStatus = "Unfollowed"
                updateFollowButtonState("Follow")
            },
            onError = {
                Toast.makeText(requireContext(), "try again", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun updateFollowButtonState(text: String) {
        binding.btnFollow.text = text
        val textColorResId =
            if (text == "Following") R.color.grey_profile else R.color.white
        val bgColorResId =
            if (text == "Following") R.drawable.rounded_rectangle else R.drawable.profile_btn

        val textColor = ContextCompat.getColor(binding.btnFollow.context, textColorResId)
        val backgroundDrawable = ContextCompat.getDrawable(binding.btnFollow.context, bgColorResId)

        binding.btnFollow.setTextColor(textColor)
        binding.btnFollow.background = backgroundDrawable
    }


    @SuppressLint("ResourceType")
    private fun createBottomSheetDialog(): BottomSheetDialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.meetball_menu, null)
        dialog.setContentView(view)
        return dialog
    }

    private fun showDialog() {
        val dialog = createBottomSheetDialog()
        val copyBtn = dialog.findViewById<View>(R.id.btn_copy_link)
        val shareBtn = dialog.findViewById<View>(R.id.btn_share_via)
        val blockBtn = dialog.findViewById<View>(R.id.btn_block)

        copyBtn?.setOnClickListener {
            dialog.dismiss()
            // TODO setup possibility to copy link
        }
        shareBtn?.setOnClickListener {
            // TODO setup possibility to share
            dialog.dismiss()
        }
        blockBtn?.setOnClickListener {
            // TODO setup possibility to block user
            dialog.dismiss()
        }
        dialog.show()
    }
}