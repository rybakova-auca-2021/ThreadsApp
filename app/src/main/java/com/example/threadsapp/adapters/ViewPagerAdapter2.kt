package com.example.threadsapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.threadsapp.view.followers.FollowersFragment
import com.example.threadsapp.view.followers.FollowingFragment
import com.example.threadsapp.view.followers.PendingFragment

class ViewPagerAdapter2(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val username: String?) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowersFragment.newInstance(username)
            1 -> FollowingFragment.newInstance(username)
            else -> Fragment()
        }
    }
}