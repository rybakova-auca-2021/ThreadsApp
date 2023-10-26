package com.example.threadsapp.view.home

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.ProfileActivity
import com.example.threadsapp.R
import com.example.threadsapp.adapters.CommentsAdapter
import com.example.threadsapp.databinding.FragmentThreadBinding
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.model.PostModel.CommentView
import com.example.threadsapp.util.CalculateTime
import com.example.threadsapp.viewModel.commentViewModel.CommentListViewModel
import com.example.threadsapp.viewModel.commentViewModel.LikeUnlikeCommentViewModel
import com.example.threadsapp.viewModel.homeViewModel.RepostViewModel
import com.example.threadsapp.viewModel.postViewModel.LikeUnlikeViewModel
import com.example.threadsapp.viewModel.postViewModel.PostDataViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ThreadFragment : Fragment() {
    private lateinit var binding:FragmentThreadBinding
    private val viewModel: PostDataViewModel by viewModels()
    private val userProfileViewModel: SomeoneProfileViewModel by viewModels()
    private val commentsViewModel: CommentListViewModel by viewModels()
    private val likeViewModel: LikeUnlikeCommentViewModel by viewModels()
    private val likeOrDislikePost: LikeUnlikeViewModel by viewModels()
    private val repostViewModel: RepostViewModel by viewModels()
    private lateinit var adapter: CommentsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var videoView: VideoView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThreadBinding.inflate(inflater, container, false)
        recyclerView = binding.commentsRecyclerView
        videoView = binding.videoView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as ProfileActivity).hideBtmNav()

        adapter = CommentsAdapter(emptyList(), SomeoneProfileViewModel())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        commentsViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressBar4.visibility = View.VISIBLE
                binding.commentsRecyclerView.visibility = View.GONE
            } else {
                binding.progressBar4.visibility = View.GONE
                binding.commentsRecyclerView.visibility = View.VISIBLE
            }
        })

        setData()
        setupNavigation()
        setClickListeners()
    }

    private fun likeOrDislike(id: Int) {
        likeViewModel.likeComment(id.toString())
    }

    private fun setClickListeners() {
        adapter.onClickListener = object : CommentsAdapter.ListClickListener<CommentView> {
            override fun onCommentClick(data: CommentView, position: Int, id: Int) {
                val action = ThreadFragmentDirections.actionToReplyFragment(id)
                findNavController().navigate(action)
            }

            override fun onRepostClick(data: CommentView, position: Int, id: Int) {
                showDialog(id)
            }

            override fun onShareClick(data: CommentView, position: Int) {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, check out this awesome app!")

                val chooserIntent = Intent.createChooser(shareIntent, "Share via")
                startActivity(chooserIntent)
            }

            override fun onLikeClick(data: CommentView, position: Int, id: Int, isLiked: Boolean) {
                likeOrDislike(id)
            }
        }
    }

    private fun setData() {
        val clickedPostId = arguments?.getInt("postId")
        var isLiked = false

        if (clickedPostId != null) {
            viewModel.readData(
                clickedPostId,
                onSuccess = { post ->
                    userProfileViewModel.getUserProfileById(
                        post.author,
                        onSuccess = { userProfile ->
                            binding.username.text = userProfile.username
                            userProfile.photo.let { photoUrl ->
                                if (photoUrl.isNullOrEmpty()) {
                                    Glide.with(binding.root.context).load(R.drawable.profile_photo)
                                        .into(binding.avatar)
                                } else {
                                    Glide.with(binding.root.context).load(photoUrl).circleCrop()
                                        .into(binding.avatar)
                                }
                            }

                            if (post.text != null) {
                                binding.threadText.text = post.text
                            } else {
                                binding.threadText.visibility = View.GONE
                            }
                            val timeDifference =
                                CalculateTime.calculateTimeDifference(post.date_posted)
                            binding.time.text = timeDifference
                            binding.likes.text = "${post.total_likes} likes"
                            isLiked = if (post.user_like) {
                                binding.likeBtn.setImageResource(R.drawable.like_btn_pressed)
                                true
                            } else {
                                binding.likeBtn.setImageResource(R.drawable.lke_btn)
                                false
                            }
                            if (post.image != null) {
                                binding.imageView4.visibility = View.VISIBLE
                                Glide.with(binding.imageView4)
                                    .load(post.image)
                                    .into(binding.imageView4)
                            } else {
                                binding.imageView4.isVisible = false
                            }
                            if (post.video != null) {
                                videoView.visibility = View.VISIBLE
                                val mediaController = MediaController(requireContext())
                                videoView.setMediaController(mediaController)
                                mediaController.setAnchorView(videoView)

                                videoView.setVideoURI(Uri.parse(post.video))
                                videoView.requestFocus()
                                videoView.start()
                            } else {
                                binding.videoView.isVisible = false
                            }

                        },
                        onError = { errorMessage ->
                            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                    setPostClickListeners(clickedPostId)
                    binding.likeBtn.setOnClickListener {
                        likeOrDislikePost(clickedPostId)
                        isLiked = !isLiked
                        updateLikeBtn(isLiked, post)
                    }
                },
                onError = {
                    Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show()
                }
            )
            setupComments(clickedPostId)
        }
    }

    private fun setPostClickListeners(id: Int) {
        binding.commentBtn.setOnClickListener {
            val action = ThreadFragmentDirections.actionToReplyFragment(id)
            findNavController().navigate(action)
        }
        binding.repostBtn.setOnClickListener {
            showDialog(id)
        }
        binding.shareBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, check out this awesome app!")

            val chooserIntent = Intent.createChooser(shareIntent, "Share via")
            startActivity(chooserIntent)
        }
    }

    private fun updateLikeBtn(isLiked: Boolean, thread: PostView) {
        if (isLiked) {
            binding.likeBtn.setImageResource(R.drawable.like_btn_pressed)
            binding.likes.text = "${thread.total_likes + 1} likes"
        } else {
            binding.likeBtn.setImageResource(R.drawable.lke_btn)
            binding.likes.text = "${thread.total_likes - 1} likes"
        }
    }

    private fun likeOrDislikePost(id: Int) {
        likeOrDislikePost.likeOrUnlike(id)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupComments(id: Int) {
        commentsViewModel.commentsList(
            id.toString(),
            onSuccess = {
                comments, count ->
                adapter.updateData(comments)
                adapter.notifyDataSetChanged()
                binding.replies.text = "$count replies"
            },
            onError = {
                Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun setupNavigation() {
        binding.btnBack.setOnClickListener{
            findNavController().navigate(R.id.homeFragment)
        }
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
            dialog.dismiss()
        }
        dialog.show()
    }
}