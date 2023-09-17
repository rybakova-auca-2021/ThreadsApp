package com.example.threadsapp.view.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentSomeoneProfileBinding
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class SomeoneProfileFragment : Fragment() {
    private lateinit var binding: FragmentSomeoneProfileBinding
    private val viewModel: SomeoneProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSomeoneProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()

        val username = arguments?.getString("username")

        if (username != null) {
            setupData(username)
        }
    }

    private fun setupNavigation() {
        binding.meetballBtn.setOnClickListener {
            showDialog()
        }
    }

    private fun setupData(username: String) {
        viewModel.getUserProfile(
            username,
            onSuccess = { userProfile ->
                userProfile.photo.let { photoUrl ->
                    if (photoUrl.isNullOrEmpty()) {
                        Glide.with(binding.root.context).load(R.drawable.profile_photo).into(binding.profilePhoto)
                    } else {
                        Glide.with(binding.root.context).load(photoUrl).circleCrop().into(binding.profilePhoto)
                    }
                }
                binding.username.text = username
                binding.name.text = userProfile.full_name
            },
            onError = { Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show() }
        )
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