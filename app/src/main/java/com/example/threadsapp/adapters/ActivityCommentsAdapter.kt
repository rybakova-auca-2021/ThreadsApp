package com.example.threadsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.model.PostModel.Comment2

class ActivityCommentsAdapter(private var comments: List<Comment2>) :
    RecyclerView.Adapter<ActivityCommentsAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_comment_view, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment2 = comments[position]
        holder.bind(comment2)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(comment2: Comment2) {
            val text = itemView.findViewById<TextView>(R.id.text)
            val commentText = itemView.findViewById<TextView>(R.id.commentText)
            val followerName = itemView.findViewById<TextView>(R.id.followerName)
            val avatar = itemView.findViewById<ImageView>(R.id.avatar)

            text.text = comment2.thread
            commentText.text = comment2.text
            followerName.text = comment2.username
            if (comment2.photo != 0) {
                Glide.with(itemView.context)
                    .load(comment2.photo).circleCrop()
                    .into(avatar)
            } else {
                avatar.setImageResource(R.drawable.profile_photo)
            }
        }
    }

    fun updateData(newList: List<Comment2>) {
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
        private val oldList: List<Comment2>,
        private val newList: List<Comment2>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
