package com.example.threadsapp.adapters

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
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
            onClickListener?.onClick(thread, position)
        }
    }

    override fun getItemCount() = threads.size

    fun setOnItemClick(listClickListener: ListClickListener<PostView>) {
        this.onClickListener = listClickListener
    }

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
                        if(thread.user_like) {
                            likeBtn.setImageResource(R.drawable.like_btn_pressed)
                            isLiked = true
                        } else {
                            likeBtn.setImageResource(R.drawable.lke_btn)
                            isLiked = false
                        }

                        binding.likeBtn.setOnClickListener {
                            isLiked = !isLiked
                            if(isLiked) {
                                likeBtn.setImageResource(R.drawable.lke_btn)
                            } else {
                                likeBtn.setImageResource(R.drawable.like_btn_pressed)
                            }
                            onClickListener?.onLikeClick(thread, adapterPosition, thread.id, isLiked)
                        }
                        binding.commentBtn.setOnClickListener {
                            onClickListener?.onCommentClick(thread, adapterPosition)
                        }
                        binding.shareBtn.setOnClickListener {
                            onClickListener?.onShareClick(thread, adapterPosition)
                        }
                        binding.repostBtn.setOnClickListener {
                            onClickListener?.onRepostClick(thread, adapterPosition)
                        }
                    }
                },
                onError = {

                }
            )
        }
    }

    interface ListClickListener<T> {
        fun onClick(data: T, position: Int)
        fun onCommentClick(data: T, position: Int)
        fun onRepostClick(data: T, position: Int)
        fun onShareClick(data: T, position: Int)
        fun onLikeClick(data: T, position: Int, id: Int, isLiked: Boolean)
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