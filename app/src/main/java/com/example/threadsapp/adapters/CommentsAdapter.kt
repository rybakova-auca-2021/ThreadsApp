package com.example.threadsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.MainThreadsBinding
import com.example.threadsapp.model.PostModel.CommentView
import com.example.threadsapp.util.CalculateTime
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class CommentsAdapter(
    private var comments: List<CommentView>,
    private val viewModel: SomeoneProfileViewModel
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    var onClickListener: ListClickListener<CommentView>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainThreadsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    override fun getItemCount() = comments.size


    fun updateData(newList: List<CommentView>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                comments,
                newList
            )
        )
        comments = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: MainThreadsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isLiked = false

        @SuppressLint("SetTextI18n")
        fun bind(comment: CommentView) {
            viewModel.getUserProfileById(
                comment.author,
                onSuccess = { userProfile ->
                    with(binding) {
                        subscribeBtn.visibility = View.GONE
                        username.text = userProfile.username
                        userProfile.photo.let { photoUrl ->
                            if (photoUrl.isNullOrEmpty()) {
                                Glide.with(binding.root.context).load(R.drawable.profile_photo)
                                    .into(binding.avatar)
                            } else {
                                Glide.with(binding.root.context).load(photoUrl).circleCrop()
                                    .into(binding.avatar)
                            }
                        }

                        threadText.text = comment.text
                        val timeDifference =
                            CalculateTime.calculateTimeDifference(comment.date_posted)
                        time.text = timeDifference
                        likes.text = "${comment.total_likes} likes"
                        isLiked = if(comment.user_like) {
                            likeBtn.setImageResource(R.drawable.like_btn_pressed)
                            true
                        } else {
                            likeBtn.setImageResource(R.drawable.lke_btn)
                            false
                        }

                        binding.likeBtn.setOnClickListener {
                            onClickListener?.onLikeClick(comment, adapterPosition, comment.id, isLiked)
                            isLiked = !isLiked
                            if(isLiked) {
                                likeBtn.setImageResource(R.drawable.lke_btn)
                            } else {
                                likeBtn.setImageResource(R.drawable.like_btn_pressed)
                            }
                        }
                        binding.commentBtn.setOnClickListener {
                            onClickListener?.onCommentClick(comment, adapterPosition, comment.id)
                        }
                        binding.shareBtn.setOnClickListener {
                            onClickListener?.onShareClick(comment, adapterPosition)
                        }
                        binding.repostBtn.setOnClickListener {
                            onClickListener?.onRepostClick(comment, adapterPosition, comment.id)
                        }
                    }
                },
                onError = {

                }
            )
        }
    }

    interface ListClickListener<T> {
        fun onCommentClick(data: T, position: Int, id: Int)
        fun onRepostClick(data: T, position: Int, id: Int)
        fun onShareClick(data: T, position: Int)
        fun onLikeClick(data: T, position: Int, id: Int, isLiked: Boolean)
    }

    class ProductDiffCallback(
        private val oldList: List<CommentView>,
        private val newList: List<CommentView>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}