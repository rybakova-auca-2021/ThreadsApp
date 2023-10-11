package com.example.threadsapp.view.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.threadsapp.R
import com.example.threadsapp.adapters.ThreadsAdapter
import com.example.threadsapp.databinding.FragmentFollowing2Binding
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.viewModel.followViewModel.FollowSomeoneViewModel
import com.example.threadsapp.viewModel.homeViewModel.FollowingViewModel
import com.example.threadsapp.viewModel.homeViewModel.RepostViewModel
import com.example.threadsapp.viewModel.postViewModel.LikeUnlikeViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class FollowingFragment : Fragment() {
    private lateinit var binding: FragmentFollowing2Binding
    private lateinit var threadsAdapter: ThreadsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private val viewModel: FollowingViewModel by viewModels()
    private val likeViewModel: LikeUnlikeViewModel by viewModels()
    private val repostViewModel: RepostViewModel by viewModels()
    private val followViewModel: FollowSomeoneViewModel by viewModels()
    private val userFollowStatusMap = mutableMapOf<Int, Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowing2Binding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        progressBar = binding.progressBar3
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = binding.swipeRefreshLayout
        progressBar.visibility = View.VISIBLE

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        threadsAdapter = ThreadsAdapter(emptyList(), SomeoneProfileViewModel())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = threadsAdapter

        setData()
    }

    private fun refreshData() {
        setupGettingData()
        swipeRefreshLayout.isRefreshing = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupGettingData() {
        viewModel.forYouPosts(
            onSuccess = { results ->
                threadsAdapter.updateData(results)
                threadsAdapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            },
            onError = {
                Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setData() {
        setupGettingData()

        threadsAdapter.onClickListener = object : ThreadsAdapter.ListClickListener<PostView> {
            override fun onClick(data: PostView, position: Int, id: Int) {
                val bundle = Bundle()
                bundle.putInt("postId", id)
                findNavController().navigate(R.id.threadFragment, bundle)
            }

            override fun onCommentClick(data: PostView, position: Int, id: Int) {
                val action = HomeFragmentDirections.actionToReplyFragment(id)
                findNavController().navigate(action)
            }

            override fun onRepostClick(data: PostView, position: Int, id: Int) {
                showDialog(data.id)
            }

            override fun onShareClick(data: PostView, position: Int) {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, check out this awesome app!")

                val chooserIntent = Intent.createChooser(shareIntent, "Share via")
                startActivity(chooserIntent)
            }

            override fun onLikeClick(data: PostView, position: Int, id: Int) {
                likeOrDislike(id)
            }

            override fun onPhotoClick(data: PostView, position: Int, id: Int) {
                val action = HomeFragmentDirections.actionToSomeoneProfile(data.author)
                findNavController().navigate(action)
            }

            override fun onFollowClick(data: PostView, position: Int, id: Int) {
                follow(data.author)
            }
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

    private fun likeOrDislike(id: Int) {
        likeViewModel.likeOrUnlike(id)
    }

    @SuppressLint("ResourceType")
    private fun createBottomSheetDialog(): BottomSheetDialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.home_repost_dialog, null)
        dialog.setContentView(view)
        return dialog
    }
    private fun showDialog(id: Int) {
        val dialog = createBottomSheetDialog()
        val repostBtn = dialog.findViewById<View>(R.id.btn_repost)
        val quoteBtn = dialog.findViewById<View>(R.id.btn_quote)

        repostBtn?.setOnClickListener {
            dialog.dismiss()
            repostViewModel.repost(id)
        }
        quoteBtn?.setOnClickListener{
            val action = HomeFragmentDirections.actionToQuoteFragment(id)
            findNavController().navigate(action)
            dialog.dismiss()
        }
        dialog.show()
    }
}