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
import com.example.threadsapp.adapters.FolloweeAdapter
import com.example.threadsapp.adapters.FollowerAdapter
import com.example.threadsapp.databinding.FragmentFollowingBinding
import com.example.threadsapp.model.ProfileModel.Followers
import com.example.threadsapp.model.ProfileModel.Follows
import com.example.threadsapp.viewModel.followViewModel.FollowSomeoneViewModel
import com.example.threadsapp.viewModel.followViewModel.FollowsViewModel
import com.example.threadsapp.viewModel.followViewModel.UnfollowViewModel

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: FollowsViewModel by viewModels()
    private val userInfoViewModel: UserInfoViewModel by viewModels()
    private val followViewModel: FollowSomeoneViewModel by viewModels()
    private val unfollowViewModel: UnfollowViewModel by viewModels()
    private lateinit var adapter: FolloweeAdapter
    private val userFollowStatusMap = mutableMapOf<Int, Boolean>()

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

        adapter = FolloweeAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener = object : FolloweeAdapter.OnItemClickListener<Follows> {
            override fun onBtnClick(data: Follows, position: Int, id: Int) {
                followBtn(id)
            }
        }

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

                for (user in results) {
                    val isFollowing = user.is_followed == "Followed"
                    userFollowStatusMap[user.followee.pk] = isFollowing
                }
            },
            onError = {
                Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show()
            }
        )
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
                Toast.makeText(requireContext(), "unfollowed", Toast.LENGTH_SHORT).show()
            },
            onError = {
                Toast.makeText(requireContext(), "try again", Toast.LENGTH_SHORT).show()
            }
        )
    }
}