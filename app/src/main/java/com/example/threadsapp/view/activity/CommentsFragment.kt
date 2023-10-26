package com.example.threadsapp.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.threadsapp.R
import com.example.threadsapp.adapters.ActivityCommentsAdapter
import com.example.threadsapp.databinding.FragmentCommentsBinding
import com.example.threadsapp.model.PostModel.Comment2

class CommentsFragment : Fragment() {
    private lateinit var binding: FragmentCommentsBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private val comments = mutableListOf<Comment2>()
    private lateinit var commentAdapter: ActivityCommentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentsBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        progressBar = binding.progressBar7
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = binding.swipeRefreshLayout
        progressBar.visibility = View.VISIBLE

        commentAdapter = ActivityCommentsAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = commentAdapter

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            loadLocalComments()
        }, 1000)
    }

    private fun refreshData() {
        loadLocalComments()
        swipeRefreshLayout.isRefreshing = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadLocalComments() {
        comments.add(Comment2( 1, "perfect full moon...","I like it too", "tomhardy", "", R.drawable.tomhardyava))
        comments.add(Comment2(2,"perfect full moon...", "\uD83D\uDE0D", "xqllrxx", "", R.drawable.xqllrxxava))
        comments.add(Comment2(3, "sometimes things go...", "fact\uD83D\uDC4D\uD83D\uDC4D", "rejectness", "", R.drawable.rejectnessava))
        comments.add(Comment2(4, "we fall in love in...", "really like this season\uD83D\uDE0D", "rejectness", "", R.drawable.rejectnessava))

        commentAdapter.updateData(comments)
        commentAdapter.notifyDataSetChanged()

        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
}