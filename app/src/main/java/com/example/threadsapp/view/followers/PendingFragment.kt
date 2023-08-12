package com.example.threadsapp.view.followers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threadsapp.R
import com.example.threadsapp.adapters.FollowerAdapter
import com.example.threadsapp.databinding.FragmentFollowingBinding
import com.example.threadsapp.databinding.FragmentPendingBinding
import com.example.threadsapp.model.Follower

class PendingFragment : Fragment() {
    private lateinit var binding: FragmentPendingBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPendingBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followersList = getFollowersList()

        val adapter = FollowerAdapter(followersList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun getFollowersList(): List<Follower> {
        return listOf(
            Follower("iamnalimov", "UX/UI", false, true),
            Follower("lily.rose", "Rosa", false, true),
            Follower("beautyguru", "Kylie", false, true)
        )
    }
}