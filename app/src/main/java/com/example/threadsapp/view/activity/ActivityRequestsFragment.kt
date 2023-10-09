package com.example.threadsapp.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threadsapp.adapters.ActivityFollowersAdapter
import com.example.threadsapp.databinding.FragmentActivityRequestsBinding
import com.example.threadsapp.viewModel.homeViewModel.ActivityViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class ActivityRequestsFragment : Fragment() {
    private lateinit var binding: FragmentActivityRequestsBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ActivityViewModel by viewModels()
    private lateinit var adapter: ActivityFollowersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityRequestsBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ActivityFollowersAdapter(emptyList(), SomeoneProfileViewModel())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        getNotifications()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getNotifications() {
        viewModel.getNotificationsByType(
            "subscribe_request",
            onSuccess = { results ->
                adapter.updateData(results)
                adapter.notifyDataSetChanged()
            })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.recyclerView.visibility = View.GONE
                binding.progressBar6.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.progressBar6.visibility = View.GONE
            }
        })
    }
}