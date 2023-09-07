package com.example.threadsapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.threadsapp.view.activity.ActivityFollowingFragment
import com.example.threadsapp.view.activity.ActivityRequestsFragment
import com.example.threadsapp.view.activity.CommentsFragment

class ActivityAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CommentsFragment()
            1 -> ActivityFollowingFragment()
            2 -> ActivityRequestsFragment()
            else -> Fragment()
        }
    }
}