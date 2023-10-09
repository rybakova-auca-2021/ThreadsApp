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
import com.example.threadsapp.adapters.ActivityCommentsAdapter
import com.example.threadsapp.databinding.FragmentCommentsBinding
import com.example.threadsapp.viewModel.homeViewModel.ActivityViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class CommentsFragment : Fragment() {
    private lateinit var binding: FragmentCommentsBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ActivityViewModel by viewModels()
    private lateinit var commentAdapter: ActivityCommentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentsBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commentAdapter = ActivityCommentsAdapter(emptyList(), SomeoneProfileViewModel())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = commentAdapter

        getNotificationsComments()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getNotificationsComments() {
        viewModel.getNotificationsByType(
            "new_comment",
            onSuccess = { results ->
                commentAdapter.updateData(results)
                commentAdapter.notifyDataSetChanged()
            })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
            }
        })
    }
}