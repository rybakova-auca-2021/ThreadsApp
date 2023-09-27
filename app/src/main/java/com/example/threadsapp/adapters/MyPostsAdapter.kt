package com.example.threadsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.ThreadViewBinding
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.util.CalculateTime
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class MyPostsAdapter(
    private var threads: List<PostView>,
    private val viewModel: SomeoneProfileViewModel
) : RecyclerView.Adapter<MyPostsAdapter.ViewHolder>() {

    var onClickListener: ListClickListener<PostView>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ThreadViewBinding.inflate(inflater, parent, false)
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
        val diffCallback = ProductDiffCallback(threads, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        threads = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < threads.size) {
            val updatedList = threads.toMutableList()
            updatedList.removeAt(position)
            threads = updatedList
            notifyItemRemoved(position)
        }
    }

    inner class ViewHolder(private val binding: ThreadViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

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
                        userProfile.photo?.let { photoUrl ->
                            if (photoUrl.isEmpty()) {
                                Glide.with(binding.root.context).load(R.drawable.profile_photo).into(binding.avatar)
                            } else {
                                Glide.with(binding.root.context).load(photoUrl).circleCrop().into(binding.avatar)
                            }
                        }

                        threadText.text = thread.text
                        val timeDifference = CalculateTime.calculateTimeDifference(thread.date_posted)
                        time.text = timeDifference
                        likes.text = "${thread.total_likes} likes"

                        binding.threadViewCard.visibility = View.VISIBLE
                        setClickListener(thread)
                    }
                },
                onError = { /* Handle error */ }
            )
        }


        private fun setClickListener(thread: PostView) {
            with(binding) {
                likeBtn.setOnClickListener {
                    onClickListener?.onLikeClick(thread, adapterPosition)
                }
                commentBtn.setOnClickListener {
                    onClickListener?.onCommentClick(thread, adapterPosition)
                }
                shareBtn.setOnClickListener {
                    onClickListener?.onShareClick(thread, adapterPosition)
                }
                repostBtn.setOnClickListener {
                    onClickListener?.onRepostClick(thread, adapterPosition)
                }
                deleteBtn.setOnClickListener {
                    onClickListener?.onDeleteClick(thread, adapterPosition, thread.id)
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
        fun onDeleteClick(data: T, position: Int, id: Int)
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
