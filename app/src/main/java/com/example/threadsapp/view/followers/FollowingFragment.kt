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
import com.example.threadsapp.model.Follower

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followersList = getFollowersList() // Replace with your list of followers

        val adapter = FollowerAdapter(followersList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun getFollowersList(): List<Follower> {
        return listOf(
            Follower("iamnalimov", "UX/UI", true, false),
            Follower("lily.rose", "Rosa", true, false),
            Follower("beautyguru", "Kylie", true, false)
        )
    }
}