package com.example.threadsapp.view.newThread

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FragmentCreateNewThreadBinding

class CreateNewThreadFragment : Fragment() {
    private lateinit var binding: FragmentCreateNewThreadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateNewThreadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textReply.setOnClickListener {
            showPopupMenu(it)
        }
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