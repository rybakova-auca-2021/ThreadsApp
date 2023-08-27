package com.example.threadsapp.view.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentSomeoneProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SomeoneProfileFragment : Fragment() {
    private lateinit var binding: FragmentSomeoneProfileBinding

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
    }

    private fun setupNavigation() {
        binding.meetballBtn.setOnClickListener {
            showDialog()
        }
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