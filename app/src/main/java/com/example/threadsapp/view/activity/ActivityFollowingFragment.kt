package com.example.threadsapp.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neobis_android_chapter8.viewModels.AuthViewModel.UserInfoViewModel
import com.example.threadsapp.adapters.ActivityFollowersAdapter
import com.example.threadsapp.adapters.ActivityRequestAdapter
import com.example.threadsapp.databinding.FragmentActivityFollowingBinding
import com.example.threadsapp.model.HomeModel.Notification
import com.example.threadsapp.viewModel.followViewModel.FollowSomeoneViewModel
import com.example.threadsapp.viewModel.followViewModel.UnfollowViewModel
import com.example.threadsapp.viewModel.homeViewModel.ActivityViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class ActivityFollowingFragment : Fragment() {
    private lateinit var binding: FragmentActivityFollowingBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: ActivityViewModel by viewModels()
    private val followViewModel: FollowSomeoneViewModel by viewModels()
    private val unfollowViewModel: UnfollowViewModel by viewModels()
    private val userInfoViewModel: SomeoneProfileViewModel by viewModels()
    private lateinit var adapter: ActivityFollowersAdapter
    private val userFollowStatusMap = mutableMapOf<Int, Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivityFollowingBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ActivityFollowersAdapter(emptyList(), SomeoneProfileViewModel())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        getNotifications()

        adapter.setOnItemClickListener = object : ActivityFollowersAdapter.OnItemClickListener<Notification> {
            override fun onBtnClick(data: Notification, position: Int, id: Int) {
                data.related_user?.let { followBtn(it) }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getNotifications() {
        viewModel.getNotificationsByType(
            "new_subscriber",
            onSuccess = { results ->
                adapter.updateData(results)
                adapter.notifyDataSetChanged()

                for (user in results) {
                    user.related_user?.let {
                        userInfoViewModel.getUserProfileById(
                            it,
                            onSuccess = { userProfile ->
                                val isFollowing = userProfile.is_followed == "Followed" || userProfile.is_followed == "Mutual Follow"
                                userFollowStatusMap[userProfile.pk] = isFollowing
                            },
                            onError = {
                            }
                        )
                    }
                }
            })
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.recyclerView.visibility = View.GONE
                binding.progressBar5.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.progressBar5.visibility = View.GONE
            }
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun followBtn(id: Int) {
        val isFollowing = userFollowStatusMap[id] ?: false
        if (isFollowing) {
            unfollow(id)
        } else {
            follow(id)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun follow(id: Int) {
        followViewModel.follow(
            id,
            onSuccess =  {
                userFollowStatusMap[id] = true
                Toast.makeText(requireContext(), "followed", Toast.LENGTH_SHORT).show()
            },
            onError = {
                Toast.makeText(requireContext(), "try again", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun unfollow(id: Int) {
        unfollowViewModel.unfollow(
            id,
            onSuccess =  {
                userFollowStatusMap[id] = false
            },
            onError = {
                Toast.makeText(requireContext(), "try again", Toast.LENGTH_SHORT).show()
            }
        )
    }
}