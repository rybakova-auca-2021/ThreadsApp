package com.example.threadsapp.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threadsapp.adapters.ActivityRequestAdapter
import com.example.threadsapp.databinding.FragmentActivityRequestsBinding
import com.example.threadsapp.model.HomeModel.Notification
import com.example.threadsapp.viewModel.followViewModel.RequestViewModel
import com.example.threadsapp.viewModel.homeViewModel.ActivityViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class ActivityRequestsFragment : Fragment() {
    private lateinit var binding: FragmentActivityRequestsBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ActivityViewModel by viewModels()
    private val requestViewModel: RequestViewModel by viewModels()
    private lateinit var adapter: ActivityRequestAdapter

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

        adapter = ActivityRequestAdapter(emptyList(), SomeoneProfileViewModel())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        getNotifications()

        adapter.setOnItemClickListener = object : ActivityRequestAdapter.OnItemClickListener<Notification> {
            override fun onConfirmClick(data: Notification, position: Int, id: Int) {
                data.related_user?.let { allowRequest(position, it) }
            }

            override fun onHideClick(data: Notification, position: Int, id: Int) {
                data.related_user?.let { declineRequest(position, it) }
            }
        }
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

    private fun allowRequest(position: Int, id: Int) {
        requestViewModel.allowRequest(id,
            onSuccess = {
                adapter.removeItem(position)
            },
            onError = {
                Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun declineRequest(position: Int, id: Int) {
        requestViewModel.declineRequest(id,
            onSuccess = {
                adapter.removeItem(position)
            },
            onError = {
                Toast.makeText(requireContext(), "Try again", Toast.LENGTH_SHORT).show()
            }
        )
    }
}