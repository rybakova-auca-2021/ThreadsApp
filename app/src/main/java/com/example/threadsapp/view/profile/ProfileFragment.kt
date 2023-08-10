package com.example.threadsapp.view.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

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
    }

    private fun setupNavigation() {
        binding.editProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        binding.logoutBtn.setOnClickListener {
            showLogoutDialog()
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

    private fun showImageOptionsBottomSheet() {
        val dialog = createBottomSheetDialog()
        val copyBtn = dialog.findViewById<View>(R.id.btn_copy)
        val shareBtn = dialog.findViewById<View>(R.id.btn_share)

        copyBtn?.setOnClickListener {
            dialog.dismiss()
            // TODO setup possibility to copy link
        }
        shareBtn?.setOnClickListener {
            // TODO setup possibility to share
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showLogoutDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout, null)
        val logoutButton: Button = dialogView.findViewById(R.id.btn_logout)
        val cancelButton: Button = dialogView.findViewById(R.id.btn_cancel)

        val alertDialog = AlertDialog.Builder(requireContext(), R.style.LightDialogTheme)
            .setView(dialogView)
            .create()

        logoutButton.setOnClickListener {
            // TODO - setup logout
        }
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}