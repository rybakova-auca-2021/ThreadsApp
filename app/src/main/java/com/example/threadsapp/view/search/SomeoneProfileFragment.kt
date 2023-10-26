package com.example.threadsapp.view.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.adapters.ThreadsAdapter
import com.example.threadsapp.databinding.FragmentSomeoneProfileBinding
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.view.home.HomeFragmentDirections
import com.example.threadsapp.view.home.ReplyFragmentArgs
import com.example.threadsapp.view.home.ThreadFragmentDirections
import com.example.threadsapp.view.profile.ProfileFragmentDirections
import com.example.threadsapp.viewModel.followViewModel.FollowSomeoneViewModel
import com.example.threadsapp.viewModel.followViewModel.MutualFollowViewModel
import com.example.threadsapp.viewModel.followViewModel.UnfollowViewModel
import com.example.threadsapp.viewModel.homeViewModel.RepostViewModel
import com.example.threadsapp.viewModel.postViewModel.LikeUnlikeViewModel
import com.example.threadsapp.viewModel.postViewModel.UserPostListViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class SomeoneProfileFragment : Fragment() {
    private lateinit var binding: FragmentSomeoneProfileBinding
    private val viewModel: SomeoneProfileViewModel by viewModels()
    private val userPostListViewModel: UserPostListViewModel by viewModels()
    private val followViewModel: FollowSomeoneViewModel by viewModels()
    private val unfollowViewModel: UnfollowViewModel by viewModels()
    private val likeViewModel: LikeUnlikeViewModel by viewModels()
    private val repostViewModel: RepostViewModel by viewModels()
    private lateinit var threadsAdapter: ThreadsAdapter
    private lateinit var recyclerView: RecyclerView
    private var userProfileId: Int = -1
    var username: String = ""
    private var followingStatus: String? = null
    private val args: SomeoneProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSomeoneProfileBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userProfileId = args.id
        setupNavigation()
        setupDataByID(userProfileId)

        threadsAdapter = ThreadsAdapter(emptyList(), SomeoneProfileViewModel())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = threadsAdapter

        userPostListViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
            }
        })
        setupGettingData(args.id)
        setupClicksOnPost()
    }

    private fun setupNavigation() {
        binding.meetballBtn.setOnClickListener {
            showDialog()
        }
        binding.btnFollow.setOnClickListener {
            followBtn(userProfileId)
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.numOfFollowers.setOnClickListener {
            val action = SomeoneProfileFragmentDirections.actionSomeoneProfileFragmentToFollowFragment(username)
            Log.d("Debug", "Username: $username")
            findNavController().navigate(action)
        }
    }

    private fun setupDataByID(id: Int) {
        viewModel.getUserProfile(
            id.toString(),
            onSuccess = { userProfile ->
                username = userProfile.username
                userProfileId = userProfile.pk
                userProfile.photo.let { photoUrl ->
                    if (photoUrl.isNullOrEmpty()) {
                        Glide.with(binding.root.context).load(R.drawable.profile_photo).into(binding.profilePhoto)
                    } else {
                        Glide.with(binding.root.context).load(photoUrl).circleCrop().into(binding.profilePhoto)
                    }
                }
                binding.numOfFollowers.text = "${userProfile.followers_count} followers"
                binding.username.text = userProfile.username
                binding.name.text = userProfile.full_name

                val initialText = when (userProfile.is_followed) {
                    "Pending" -> "Requested"
                    "Mutual Follow" -> "Following"
                    "Followed" -> "Following"
                    else -> "Follow"
                }
                followingStatus = userProfile.is_followed
                updateFollowButtonState(initialText)
            },
            onError = { Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show() }
        )
    }
    private fun setupClicksOnPost() {
        threadsAdapter.onClickListener = object : ThreadsAdapter.ListClickListener<PostView> {
            override fun onClick(data: PostView, position: Int, id: Int) {
                val bundle = Bundle()
                bundle.putInt("postId", id)
                findNavController().navigate(R.id.threadFragment, bundle)
            }

            override fun onCommentClick(data: PostView, position: Int, id: Int) {
                val action = SomeoneProfileFragmentDirections.actionToReplyFragment(id)
                findNavController().navigate(action)
            }

            override fun onRepostClick(data: PostView, position: Int, id: Int) {
                showRepostDialog(id)
            }

            override fun onShareClick(data: PostView, position: Int) {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, check out this awesome app!")

                val chooserIntent = Intent.createChooser(shareIntent, "Share via")
                startActivity(chooserIntent)
            }

            override fun onLikeClick(data: PostView, position: Int, id: Int) {
                likeOrDislike(id)
            }

            override fun onPhotoClick(data: PostView, position: Int, id: Int) {
            }

            override fun onFollowClick(data: PostView, position: Int, id: Int) {
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun followBtn(id: Int) {
        when (followingStatus) {
            "Followed", "Mutual Follow" -> {
                unfollow(id)
            }
            "Not Followed" -> {
                follow(id)
            }
            else -> {
                updateFollowButtonState("Follow")
            }
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
                followingStatus = "Not Followed"
                updateFollowButtonState("Follow")
            },
            onError = {
                Toast.makeText(requireContext(), "try again", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun likeOrDislike(id: Int) {
        likeViewModel.likeOrUnlike(id)
    }

    private fun updateFollowButtonState(text: String) {
        binding.btnFollow.text = text
        val textColorResId =
            if (text == "Following" || text == "Requested") R.color.grey_profile else R.color.white
        val bgColorResId =
            if (text == "Following" || text == "Requested") R.drawable.rounded_rectangle else R.drawable.profile_btn

        val textColor = ContextCompat.getColor(binding.btnFollow.context, textColorResId)
        val backgroundDrawable = ContextCompat.getDrawable(binding.btnFollow.context, bgColorResId)

        binding.btnFollow.setTextColor(textColor)
        binding.btnFollow.background = backgroundDrawable
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupGettingData(id: Int) {
        userPostListViewModel.userPostList(
            id,
            onSuccess = { results ->
                binding.privateCheck.visibility = View.GONE
                threadsAdapter.updateData(results)
                threadsAdapter.notifyDataSetChanged()
            },
            onError = {
                binding.privateCheck.visibility = View.VISIBLE
            }
        )
    }

    @SuppressLint("ResourceType")
    private fun createBottomSheetDialog(): BottomSheetDialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.meetball_menu, null)
        dialog.setContentView(view)
        return dialog
    }

    @SuppressLint("ResourceType")
    private fun createBottomSheetRepostDialog(): BottomSheetDialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.home_repost_dialog, null)
        dialog.setContentView(view)
        return dialog
    }
    private fun showRepostDialog(id: Int) {
        val dialog = createBottomSheetRepostDialog()
        val repostBtn = dialog.findViewById<View>(R.id.btn_repost)
        val quoteBtn = dialog.findViewById<View>(R.id.btn_quote)

        repostBtn?.setOnClickListener {
            dialog.dismiss()
            repostViewModel.repost(id)
        }
        quoteBtn?.setOnClickListener{
            val action = HomeFragmentDirections.actionToQuoteFragment(id)
            findNavController().navigate(action)
            dialog.dismiss()
        }
        dialog.show()
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