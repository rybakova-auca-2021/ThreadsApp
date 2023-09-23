package com.example.threadsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.databinding.MainThreadsBinding
import com.example.threadsapp.model.HomeModel.PostView

class ThreadsAdapter(private var threads: List<PostView>) : RecyclerView.Adapter<ThreadsAdapter.ViewHolder>() {

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

        @SuppressLint("SetTextI18n")
        fun bind(thread: PostView) {
            with(binding) {
                if (thread.image != null) {
                    Glide.with(imageView4)
                        .load(thread.image)
                        .into(imageView4)
                } else {
                    imageView4.isVisible = false
                }
                threadText.text = thread.text
                time.text = thread.date_posted
                likes.text = "${thread.total_likes} likes"

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