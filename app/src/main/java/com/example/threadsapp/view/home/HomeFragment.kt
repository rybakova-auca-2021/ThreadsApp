package com.example.threadsapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.threadsapp.ProfileActivity
import com.example.threadsapp.adapters.HomeAdapter
import com.example.threadsapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        (requireActivity() as ProfileActivity).showBtmNav()

        val tabLayout = binding.tabLayout
        val viewPager2 = binding.fragmentHolder

        viewPager2.adapter = HomeAdapter(parentFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "For you"
                1 -> tab.text = "Following"
            }
        }.attach()

        return binding.root
    }
}