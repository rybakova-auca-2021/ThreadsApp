package com.example.threadsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.ActivityCommentViewBinding
import com.example.threadsapp.model.HomeModel.Notification
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class ActivityCommentsAdapter(var comments: List<Notification>, private val viewModel: SomeoneProfileViewModel) :
    RecyclerView.Adapter<ActivityCommentsAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding =
            ActivityCommentViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    inner class CommentViewHolder(private val binding: ActivityCommentViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Notification) {
            comment.related_user?.let {
                viewModel.getUserProfileById(
                    it,
                    onSuccess = { userProfile ->
                        binding.followerName.text = userProfile.username
                        userProfile.photo.let { photoUrl ->
                            if (photoUrl.isNullOrEmpty()) {
                                Glide.with(binding.root.context).load(R.drawable.profile_photo)
                                    .into(binding.avatar!!)
                            } else {
                                Glide.with(binding.root.context).load(photoUrl).circleCrop()
                                    .into(binding.avatar!!)
                            }
                        }
                        binding.text.text = comment.text
                    },
                    onError = {

                    })
            }
        }
    }

    fun updateData(newList: List<Notification>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                comments,
                newList
            )
        )
        comments = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class ProductDiffCallback(
        private val oldList: List<Notification>,
        private val newList: List<Notification>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].pk == newList[newItemPosition].pk

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
