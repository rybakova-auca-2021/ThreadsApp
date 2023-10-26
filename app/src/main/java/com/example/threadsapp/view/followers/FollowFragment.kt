package com.example.threadsapp.view.followers

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.threadsapp.R
import com.example.threadsapp.adapters.ViewPagerAdapter
import com.example.threadsapp.adapters.ViewPagerAdapter2
import com.example.threadsapp.databinding.FragmentFollowBinding
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator

class FollowFragment : Fragment() {
    private lateinit var binding: FragmentFollowBinding
    private val viewModel: SomeoneProfileViewModel by viewModels()
    private var username: String? = null
    private var isFollowed: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)

        username = arguments?.getString("username")

        username?.let {
            viewModel.getUserProfile(it,
                onSuccess = { profile ->
                    isFollowed = profile.is_followed
                },
                onError = {
                    println("failed to get is followed")
                }
            )
        }

        val tabLayout = binding.tabLayout
        val viewPager2 = binding.fragmentHolder

        if (isFollowed == "You") {
            viewPager2.adapter = username?.let {
                ViewPagerAdapter2(parentFragmentManager, lifecycle, it)
            }
        } else {
            viewPager2.adapter = username?.let {
                ViewPagerAdapter(parentFragmentManager, lifecycle, it)
            }
        }

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Followers"
                1 -> tab.text = "Following"
                2 -> tab.text = "Pending"
            }
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDialog()
        setupNavigation()
        binding.username.text = username
    }

    private fun setupNavigation() {
        binding.closeBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupDialog() {
        binding.sortBtn.setOnClickListener {
            showImageOptionsBottomSheet()
        }
    }

    @SuppressLint("ResourceType")
    private fun createBottomSheetDialog(): BottomSheetDialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.sort_dialog, null)
        dialog.setContentView(view)
        return dialog
    }

    private fun showImageOptionsBottomSheet() {
        val dialog = createBottomSheetDialog()
        val defaultBtn = dialog.findViewById<View>(R.id.btn_default)
        val latestBtn = dialog.findViewById<View>(R.id.btn_latest)
        val earliestBtn = dialog.findViewById<View>(R.id.btn_earliest)

        defaultBtn?.setOnClickListener {
            dialog.dismiss()
            // TODO
        }
        latestBtn?.setOnClickListener {
            // TODO
            dialog.dismiss()
        }
        earliestBtn?.setOnClickListener {
            // TODO
            dialog.dismiss()
        }
        dialog.show()
    }
}

