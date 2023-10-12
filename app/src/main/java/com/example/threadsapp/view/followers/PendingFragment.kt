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
import com.example.threadsapp.adapters.RequestsAdapter
import com.example.threadsapp.databinding.FragmentPendingBinding
import com.example.threadsapp.model.ProfileModel.Followers
import com.example.threadsapp.viewModel.followViewModel.PendingViewModel

class PendingFragment : Fragment() {
    private lateinit var binding: FragmentPendingBinding
    private lateinit var recyclerView: RecyclerView
    private val viewModel: PendingViewModel by viewModels()
    private lateinit var adapter: RequestsAdapter

    companion object {
        fun newInstance(username: String?): PendingFragment {
            val fragment = PendingFragment()
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
        binding = FragmentPendingBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RequestsAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        getListOfFollowRequests()

        adapter.setOnItemClickListener = object : RequestsAdapter.OnItemClickListener<Followers> {
            override fun onBtnClick(data: Followers, position: Int, id: Int) {
                adapter.removeItem(data.follower.pk)
            }

            override fun onItemClick(data: Followers, position: Int, id: Int, isFollowed: String) {
                val action = FollowFragmentDirections.actionToSomeoneProfile(id, isFollowed)
                findNavController().navigate(action)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getListOfFollowRequests() {
        viewModel.pendingList(
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