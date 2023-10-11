package com.example.threadsapp.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.neobis_android_chapter8.viewModels.AuthViewModel.UserInfoViewModel
import com.example.threadsapp.adapters.ActivityFollowersAdapter
import com.example.threadsapp.adapters.ActivityRequestAdapter
import com.example.threadsapp.databinding.FragmentActivityFollowingBinding
import com.example.threadsapp.model.HomeModel.Notification
import com.example.threadsapp.view.followers.FollowFragmentDirections
import com.example.threadsapp.viewModel.followViewModel.FollowSomeoneViewModel
import com.example.threadsapp.viewModel.followViewModel.UnfollowViewModel
import com.example.threadsapp.viewModel.homeViewModel.ActivityViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class ActivityFollowingFragment : Fragment() {
    private lateinit var binding: FragmentActivityFollowingBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
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
        progressBar = binding.progressBar5
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = binding.swipeRefreshLayout
        progressBar.visibility = View.VISIBLE

        adapter = ActivityFollowersAdapter(emptyList(), SomeoneProfileViewModel())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        Handler(Looper.getMainLooper()).postDelayed({
            getNotifications()
        }, 1000)

        adapter.setOnItemClickListener = object : ActivityFollowersAdapter.OnItemClickListener<Notification> {
            override fun onBtnClick(data: Notification, position: Int, id: Int) {
                data.related_user?.let { followBtn(it) }
            }

            override fun onItemClick(data: Notification, position: Int, id: Int) {
                val action = data.related_user?.let {
                    ActivityFragmentDirections.actionToSomeoneProfile(
                        it
                    )
                }
                if (action != null) {
                    findNavController().navigate(action)
                }
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        getNotifications()
        swipeRefreshLayout.isRefreshing = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getNotifications() {
        viewModel.getNotificationsByType(
            "new_subscriber",
            onSuccess = { results ->
                val uniqueNotificationsMap = HashMap<Int, Notification>()
                for (notification in results) {
                    val relatedUserId = notification.related_user ?: continue
                    val existingNotification = uniqueNotificationsMap[relatedUserId]

                    if (existingNotification == null || notification.date_posted > existingNotification.date_posted) {
                        uniqueNotificationsMap[relatedUserId] = notification
                    }
                }
                val uniqueNotificationsList = uniqueNotificationsMap.values.toList()

                adapter.updateData(uniqueNotificationsList)
                adapter.notifyDataSetChanged()

                for (notification in uniqueNotificationsList) {
                    notification.related_user?.let {
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
            }
        )
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