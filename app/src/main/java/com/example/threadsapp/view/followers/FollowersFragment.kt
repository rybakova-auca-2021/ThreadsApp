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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.neobis_android_chapter8.viewModels.AuthViewModel.UserInfoViewModel
import com.example.threadsapp.adapters.FollowerAdapter
import com.example.threadsapp.databinding.FragmentFollowersBinding
import com.example.threadsapp.model.ProfileModel.Followers
import com.example.threadsapp.viewModel.followViewModel.FollowSomeoneViewModel
import com.example.threadsapp.viewModel.followViewModel.FollowersViewModel
import com.example.threadsapp.viewModel.followViewModel.UnfollowViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class FollowersFragment : Fragment() {
    private lateinit var binding: FragmentFollowersBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val viewModel: FollowersViewModel by viewModels()
    private val userInfoViewModel: SomeoneProfileViewModel by viewModels()
    private val followViewModel: FollowSomeoneViewModel by viewModels()
    private val unfollowViewModel: UnfollowViewModel by viewModels()
    private lateinit var adapter: FollowerAdapter
    private val userFollowStatusMap = mutableMapOf<Int, Boolean>()
    private val args: FollowersFragmentArgs by navArgs()
    var username = ""

    companion object {
        fun newInstance(username: String?): FollowersFragment {
            val fragment = FollowersFragment()
            val args = Bundle()
            args.putString("username", username)
            fragment.arguments = args
            return fragment
        }
    }

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
        username = args.username
        swipeRefreshLayout = binding.swipeRefreshLayout

        adapter = FollowerAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener = object : FollowerAdapter.OnItemClickListener<Followers> {
            override fun onBtnClick(data: Followers, position: Int, id: Int) {
                followBtn(id, position)
            }

            override fun onItemClick(data: Followers, position: Int, id: Int, isFollowed: String) {
                val action = FollowFragmentDirections.actionToSomeoneProfile(id, isFollowed)
                findNavController().navigate(action)
            }
        }

        userInfoViewModel.getUserProfile(
            username,
            onSuccess = { profile ->
                val userId = profile.pk
                getListOfFollowers(userId)
                swipeRefreshLayout.setOnRefreshListener {
                    getListOfFollowers(userId)
                    swipeRefreshLayout.isRefreshing = false
                }
            },
            onError = {
                println("try again")
            }
        )

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
    private fun followBtn(id: Int, position: Int) {
        val isFollowing = userFollowStatusMap[id] ?: false
        if (isFollowing) {
            unfollow(id, position)
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

    private fun unfollow(id: Int, position: Int) {
        unfollowViewModel.unfollow(
            id,
            onSuccess =  {
                userFollowStatusMap[id] = false
                adapter.removeItem(position)
            },
            onError = {
                Toast.makeText(requireContext(), "try again", Toast.LENGTH_SHORT).show()
            }
        )
    }
}