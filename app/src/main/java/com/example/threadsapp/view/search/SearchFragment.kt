package com.example.threadsapp.view.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threadsapp.adapters.SearchResultAdapter
import com.example.threadsapp.databinding.FragmentSearchBinding
import com.example.threadsapp.model.UserResult
import com.example.threadsapp.viewModel.followViewModel.FollowSomeoneViewModel
import com.example.threadsapp.viewModel.followViewModel.UnfollowViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel
import com.example.threadsapp.viewModel.searchViewModel.SearchViewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: SearchViewModel by viewModels()
    private val userInfoViewModel: SomeoneProfileViewModel by viewModels()
    private val followViewModel: FollowSomeoneViewModel by viewModels()
    private val unfollowViewModel: UnfollowViewModel by viewModels()
    private lateinit var adapter: SearchResultAdapter
    private val userFollowStatusMap = mutableMapOf<Int, Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerViewSearch
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SearchResultAdapter(emptyList(), userInfoViewModel)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener = object : SearchResultAdapter.OnItemClickListener<UserResult> {
            override fun onItemClick(data: UserResult, position: Int, id: Int) {
                val action = SearchFragmentDirections.actionToSomeoneProfile(data.username, data.is_followed)
                findNavController().navigate(action)
            }

            override fun onBtnClick(data: UserResult, position: Int, id: Int) {
                followBtn(id)
            }

        }
        search()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun performSearch(query: String) {
        viewModel.searchForUser(query,
            onSuccess = { searchResults ->
                println("SearchResults")
                adapter.updateData(searchResults)
                adapter.notifyDataSetChanged()

                var isFollowing = false
                for (user in searchResults) {
                    val isFollowing = user.is_followed == "Followed"
                    userFollowStatusMap[user.pk] = isFollowing
                }
            },
            onError = {
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

    private fun search() {
        binding.etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { performSearch(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { performSearch(it) }
                return true
            }
        })
    }
}