package com.example.threadsapp.view.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.threadsapp.R
import com.example.threadsapp.adapters.ThreadsAdapter
import com.example.threadsapp.databinding.FragmentForYouBinding
import com.example.threadsapp.model.ThreadData
import com.google.android.material.bottomsheet.BottomSheetDialog


class ForYouFragment : Fragment() {
    private lateinit var binding: FragmentForYouBinding
    private lateinit var threadsAdapter: ThreadsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForYouBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupThreadsAdapter()
        loadSampleThreads()
    }

    private fun setupThreadsAdapter() {
        threadsAdapter = ThreadsAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = threadsAdapter
        }
    }

    private fun loadSampleThreads() {
        val sampleThreads = listOf(
            ThreadData(R.drawable.avatar, "kamiloyy", "Opportunities don't happen, you create them.", "1h", "20 replies", "400 likes", R.drawable.thread_img),
            ThreadData(R.drawable.avatar, "alinoyy", "Opportunities don't happen, you create them.", "2h", "40 replies", "100 likes", R.drawable.image),
            ThreadData(R.drawable.avatar, "kamiloyy", "Opportunities don't happen, you create them.", "1h", "20 replies", "400 likes", R.drawable.thread_img),
            ThreadData(R.drawable.avatar, "alinoyy", "Opportunities don't happen, you create them.", "2h", "40 replies", "100 likes", R.drawable.image),
            ThreadData(R.drawable.avatar, "kamiloyy", "Opportunities don't happen, you create them.", "1h", "20 replies", "400 likes", R.drawable.thread_img),
            ThreadData(R.drawable.avatar, "alinoyy", "Opportunities don't happen, you create them.", "2h", "40 replies", "100 likes", R.drawable.image),
            )

        threadsAdapter.threads = sampleThreads

        threadsAdapter.onClickListener = object : ThreadsAdapter.ListClickListener<ThreadData> {
            override fun onClick(data: ThreadData, position: Int) {
                val action = HomeFragmentDirections.actionToThreadFragment(data)
                findNavController().navigate(action)
            }

            override fun onCommentClick(data: ThreadData, position: Int) {
                val action = HomeFragmentDirections.actionToReplyFragment(data)
                findNavController().navigate(action)
            }

            override fun onRepostClick(data: ThreadData, position: Int) {
                showDialog()
            }

            override fun onShareClick(data: ThreadData, position: Int) {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, check out this awesome app!")

                val chooserIntent = Intent.createChooser(shareIntent, "Share via")
                startActivity(chooserIntent)
            }

            override fun onLikeClick(data: ThreadData, position: Int) {
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
