package com.example.threadsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.MainThreadsBinding
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.util.CalculateTime
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class ThreadsAdapter(
    private var threads: List<PostView>,
    private val viewModel: SomeoneProfileViewModel
) : RecyclerView.Adapter<ThreadsAdapter.ViewHolder>() {

    var onClickListener: ListClickListener<PostView>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainThreadsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thread = threads[position]
        holder.bind(thread)
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(thread, position, thread.id)
        }
    }

    override fun getItemCount() = threads.size

    fun updateData(newList: List<PostView>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                threads,
                newList
            )
        )
        threads = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: MainThreadsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isLiked = false

        @SuppressLint("SetTextI18n")
        fun bind(thread: PostView) {
            viewModel.getUserProfileById(
                thread.author,
                onSuccess = { userProfile ->
                    with(binding) {
                        if (thread.image != null) {
                            Glide.with(imageView4)
                                .load(thread.image)
                                .into(imageView4)
                        } else {
                            imageView4.isVisible = false
                        }

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

                        threadText.text = thread.text
                        val timeDifference =
                            CalculateTime.calculateTimeDifference(thread.date_posted)
                        time.text = timeDifference
                        likes.text = "${thread.total_likes} likes"
                        isLiked = if (thread.user_like) {
                            likeBtn.setImageResource(R.drawable.like_btn_pressed)
                            true
                        } else {
                            likeBtn.setImageResource(R.drawable.lke_btn)
                            false
                        }

                        binding.likeBtn.setOnClickListener {
                            onClickListener?.onLikeClick(thread, adapterPosition, thread.id)
                            isLiked = !isLiked
                            updateLikeBtn(isLiked, thread)
                        }
                        binding.commentBtn.setOnClickListener {
                            onClickListener?.onCommentClick(thread, adapterPosition, thread.id)
                        }
                        binding.shareBtn.setOnClickListener {
                            onClickListener?.onShareClick(thread, adapterPosition)
                        }
                        binding.repostBtn.setOnClickListener {
                            onClickListener?.onRepostClick(thread, adapterPosition, thread.id)
                        }
                        binding.avatar.setOnClickListener {
                            onClickListener?.onPhotoClick(thread, adapterPosition, thread.id)
                        }
                        binding.subscribeBtn.setOnClickListener {
                            onClickListener?.onFollowClick(thread, adapterPosition, thread.id)
                            binding.subscribeBtn.visibility = View.GONE
                        }
                    }
                },
                onError = {

                }
            )
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
    }

    interface ListClickListener<T> {
        fun onClick(data: T, position: Int, id: Int)
        fun onCommentClick(data: T, position: Int, id: Int)
        fun onRepostClick(data: T, position: Int, id: Int)
        fun onShareClick(data: T, position: Int)
        fun onLikeClick(data: T, position: Int, id: Int)
        fun onPhotoClick(data: T, position: Int, id: Int)
        fun onFollowClick(data: T, position: Int, id: Int)
    }

    class ProductDiffCallback(
        private val oldList: List<PostView>,
        private val newList: List<PostView>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}