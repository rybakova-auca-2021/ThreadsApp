package com.example.threadsapp.view.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.neobis_android_chapter8.viewModels.AuthViewModel.UserInfoViewModel
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: UserInfoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        showUserData()
        viewModel.getInfo()
    }

    private fun setupNavigation() {
        binding.editProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        binding.logoutBtn.setOnClickListener {
            showLogoutDialog()
        }
        binding.numOfFollowers.setOnClickListener {
            findNavController().navigate(R.id.followFragment)
        }
        binding.shareProfileBtn.setOnClickListener {
            showImageOptionsBottomSheet()
        }
    }

    @SuppressLint("ResourceType")
    private fun createBottomSheetDialog(): BottomSheetDialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.share_profile_bottom_sheet, null)
        dialog.setContentView(view)
        return dialog
    }

    private fun showUserData() {
        viewModel.profileData.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                binding.name.setText(it.full_name)
                binding.username.setText(it.username)
//                profile.photo.let { photoUrl ->
//                    if (it.photo.isNullOrEmpty()) {
//                        Glide.with(this).load(R.drawable.profile_photo).into(binding.profilePhoto)
//                    } else {
//                        val photoUrl = it.photo
//                        Glide.with(this).load(photoUrl).circleCrop().into(binding.profilePhoto)
//                    }
//                }
            }
        }
    }

    private fun showImageOptionsBottomSheet() {
        val dialog = createBottomSheetDialog()
        val copyBtn = dialog.findViewById<View>(R.id.btn_copy_link)
        val shareBtn = dialog.findViewById<View>(R.id.btn_share_via)

        copyBtn?.setOnClickListener {
            dialog.dismiss()
            // TODO setup possibility to copy link
        }
        shareBtn?.setOnClickListener {
            setupShare()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setupShare() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, check out this awesome app!")

        val chooserIntent = Intent.createChooser(shareIntent, "Share via")
        startActivity(chooserIntent)
    }

    private fun showLogoutDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout, null)
        val logoutButton: Button = dialogView.findViewById(R.id.btn_logout)
        val cancelButton: Button = dialogView.findViewById(R.id.btn_cancel)

        val alertDialog = AlertDialog.Builder(requireContext(), R.style.LightDialogTheme)
            .setView(dialogView)
            .create()

        logoutButton.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}