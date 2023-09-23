package com.example.threadsapp.view.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threadsapp.R
import com.example.threadsapp.adapters.ThreadsAdapter
import com.example.threadsapp.databinding.FragmentFollowing2Binding
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.viewModel.homeViewModel.FollowingViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowing2Binding
    private lateinit var adapter: ThreadsAdapter
    private lateinit var recyclerView: RecyclerView
    private val viewModel: FollowingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowing2Binding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupThreadsAdapter()
        setData()
    }

    private fun setupThreadsAdapter() {
        adapter = ThreadsAdapter(emptyList(), SomeoneProfileViewModel())

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapter
        }
    }

    private fun setupGettingData() {
        viewModel.forYouPosts(
            onSuccess = { results ->
                adapter.updateData(results)
                adapter.notifyDataSetChanged()
            },
            onError = {
                Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setData() {
        setupGettingData()

        adapter.onClickListener = object : ThreadsAdapter.ListClickListener<PostView> {
            override fun onClick(data: PostView, position: Int) {
//                val action = HomeFragmentDirections.actionToThreadFragment(data)
//                findNavController().navigate(action)
            }

            override fun onCommentClick(data: PostView, position: Int) {
//                val action = HomeFragmentDirections.actionToReplyFragment(data)
//                findNavController().navigate(action)
            }

            override fun onRepostClick(data: PostView, position: Int) {
                showDialog()
            }

            override fun onShareClick(data: PostView, position: Int) {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, check out this awesome app!")

                val chooserIntent = Intent.createChooser(shareIntent, "Share via")
                startActivity(chooserIntent)
            }

            override fun onLikeClick(data: PostView, position: Int) {
                // Handle like click
            }
        }
    }

    @SuppressLint("ResourceType")
    private fun createBottomSheetDialog(): BottomSheetDialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.home_repost_dialog, null)
        dialog.setContentView(view)
        return dialog
    }
    private fun showDialog() {
        val dialog = createBottomSheetDialog()
        val repostBtn = dialog.findViewById<View>(R.id.repost_btn)
        val quoteBtn = dialog.findViewById<View>(R.id.btn_quote)

        repostBtn?.setOnClickListener {
            dialog.dismiss()
            // TODO
        }
        quoteBtn?.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }
}