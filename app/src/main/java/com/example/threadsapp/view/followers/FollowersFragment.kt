package com.example.threadsapp.view.followers

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neobis_android_chapter8.viewModels.AuthViewModel.UserInfoViewModel
import com.example.threadsapp.adapters.FollowerAdapter
import com.example.threadsapp.databinding.FragmentFollowersBinding
import com.example.threadsapp.model.ProfileModel.Followers
import com.example.threadsapp.viewModel.followViewModel.FollowSomeoneViewModel
import com.example.threadsapp.viewModel.followViewModel.FollowersViewModel
import com.example.threadsapp.viewModel.followViewModel.UnfollowViewModel

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: FollowersViewModel by viewModels()
    private val userInfoViewModel: UserInfoViewModel by viewModels()
    private val followViewModel: FollowSomeoneViewModel by viewModels()
    private val unfollowViewModel: UnfollowViewModel by viewModels()
    private lateinit var adapter: FollowerAdapter
    private val userFollowStatusMap = mutableMapOf<Int, Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowerAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener = object : FollowerAdapter.OnItemClickListener<Followers> {
            override fun onBtnClick(data: Followers, position: Int, id: Int) {
                followBtn(id)
            }

            override fun onItemClick(data: Followers, position: Int, id: Int, isFollowed: String) {
                val action = FollowFragmentDirections.actionToSomeoneProfile(id, isFollowed)
                findNavController().navigate(action)
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
        viewModel.followersList(
            userId,
            onSuccess = { results ->
                adapter.updateData(results)
                adapter.notifyDataSetChanged()

                for (user in results) {
                    val isFollowing = user.is_followed == "Followed"
                    userFollowStatusMap[user.follower.pk] = isFollowing
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