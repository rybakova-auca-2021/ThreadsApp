package com.example.threadsapp.adapters

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ActivityAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private var fragments: Array<Fragment> = arrayOf(Fragment(), Fragment())

    @SuppressLint("NotifyDataSetChanged")
    fun setFragments(fragmentArray: Array<Fragment>) {
        if (fragmentArray.size == 2) {
            fragments = fragmentArray
            notifyDataSetChanged()
        } else {
            throw IllegalArgumentException("Fragment array must have size 2")
        }
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}