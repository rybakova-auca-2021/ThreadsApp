package com.example.threadsapp.view.followers

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neobis_android_chapter8.viewModels.AuthViewModel.UserInfoViewModel
import com.example.threadsapp.adapters.FollowerAdapter
import com.example.threadsapp.databinding.FragmentFollowingBinding
import com.example.threadsapp.viewModel.followViewModel.FollowsViewModel

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: FollowsViewModel by viewModels()
    private val userInfoViewModel: UserInfoViewModel by viewModels()
    private lateinit var adapter: FollowerAdapter

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

        adapter = FollowerAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        userInfoViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            if (profile != null) {
                val userId = profile.pk
                getListOfFollowers(userId)
            } else {
            }
        }
        userInfoViewModel.getInfo()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getListOfFollowers(userId: Int) {
        viewModel.followsList(
            userId,
            onSuccess = { results ->
                adapter.updateData(results)
                adapter.notifyDataSetChanged()
            },
            onError = {
                Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show()
            }
        )
    }
}