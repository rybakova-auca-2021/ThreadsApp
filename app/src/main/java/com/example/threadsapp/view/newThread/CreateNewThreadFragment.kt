package com.example.threadsapp.view.newThread

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentCreateNewThreadBinding
import com.example.threadsapp.util.Utils

class CreateNewThreadFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewThreadBinding
    private var PICK_IMAGE_REQUEST  = 1
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNewThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupCharacterCount()
    }

    private fun setupNavigation() {
        binding.addMedia.setOnClickListener {
            chooseImage()
        }
        binding.textReply.setOnClickListener {
            showPopupMenu(it)
        }
        binding.btnDeleteText.setOnClickListener {
            removeData()
        }
        binding.postBtn.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
    }

    private fun setupCharacterCount() {
        binding.startThread.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if ((s?.length ?: 0) >= 300) {
                    binding.startThread.setTextColor(Color.RED)
                    binding.postBtn.isEnabled = false
                } else {
                    binding.startThread.setTextColor(Color.BLACK)
                    binding.postBtn.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    private fun removeData() {
        selectedImageUri = null
        binding.apply {
            imageView10.visibility = View.GONE
            btnDeleteText.visibility = View.GONE
            profilePhotoThread.visibility = View.GONE
            addThread.visibility = View.GONE
            addMedia.visibility = View.VISIBLE
        }

        binding.startThread.text.clear()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            Utils.selectedImageUri = selectedImageUri
            Glide.with(this)
                .load(selectedImageUri)
                .override(300, 350)
                .into(binding.imageView10)

            binding.apply {
                imageView10.visibility = View.VISIBLE
                btnDeleteText.visibility = View.VISIBLE
                profilePhotoThread.visibility = View.VISIBLE
                addThread.visibility = View.VISIBLE
                addMedia.visibility = View.GONE
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.popup_menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.optionAnyone -> {
                    true
                }
                R.id.optionProfilesFollow -> {
                    true
                }
                R.id.optionMentionedOnly -> {
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}