package com.example.threadsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.databinding.MainThreadsBinding
import com.example.threadsapp.model.ThreadData

class ThreadsAdapter : RecyclerView.Adapter<ThreadsAdapter.ViewHolder>() {

    var threads = emptyList<ThreadData>()
    var onClickListener: ListClickListener<ThreadData>? = null

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

    fun setOnItemClick(listClickListener: ListClickListener<ThreadData>) {
        this.onClickListener = listClickListener
    }

    inner class ViewHolder(val binding: MainThreadsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(thread: ThreadData) {
            with(binding) {
                if (thread.images != null && thread.images != 0) {
                    Glide.with(imageView4)
                        .load(thread.images)
                        .into(imageView4)
                } else {
                    imageView4.isVisible = false
                }
                username.text = thread.username
                threadText.text = thread.text
                time.text = thread.time
                likes.text = thread.likes

                binding.likeBtn.setOnClickListener {
                    onClickListener?.onLikeClick(thread, adapterPosition)
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
        }
    }

    interface ListClickListener<T> {
        fun onClick(data: T, position: Int)
        fun onCommentClick(data: T, position: Int)
        fun onRepostClick(data: T, position: Int)
        fun onShareClick(data: T, position: Int)
        fun onLikeClick(data: T, position: Int)
    }
}