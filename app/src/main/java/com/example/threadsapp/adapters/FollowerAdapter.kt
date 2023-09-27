package com.example.threadsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.FollowerViewBinding
import com.example.threadsapp.databinding.MainThreadsBinding
import com.example.threadsapp.model.Follower
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.model.ProfileModel.Followers

class FollowerAdapter(private var followers: List<Followers>) :
    RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val followerPhotoImageView = itemView.findViewById<ImageView>(R.id.imageView)
        val followerNameTextView = itemView.findViewById<TextView>(R.id.followerName)
        val nameTextView = itemView.findViewById<TextView>(R.id.job)
        val followButton = itemView.findViewById<Button>(R.id.followButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.follower_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val follower = followers[position]

        holder.followerNameTextView.text = follower.follower.username
        holder.nameTextView.text = follower.follower.full_name
        Glide.with(holder.followerPhotoImageView).load(follower.follower.photo).into(holder.followerPhotoImageView)

//        if (follower.is_followed) {
//            holder.followButton.text = "Following"
//            holder.followButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey_auth))
//        } else if (follower.isRequested) {
//            holder.followButton.text = "Cancel"
//            holder.followButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey_auth))
//        } else {
//            holder.followButton.text = "Follow"
//            holder.followButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
//        }
//
//
//        holder.followButton.setOnClickListener {
//            if (follower.isFollowing) {
//                follower.isFollowing = false
//                holder.followButton.text = "Follow"
//                holder.followButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
//            } else if (follower.isRequested) {
//                follower.isRequested = false
//                holder.followButton.text = "Follow"
//                holder.followButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
//            } else {
//                follower.isFollowing = true
//                holder.followButton.text = "Following"
//                holder.followButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey_auth))
//            }
//        }
    }

    override fun getItemCount(): Int {
        return followers.size
    }

    fun updateData(newList: List<Followers>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                followers,
                newList
            )
        )
        followers = newList
        diffResult.dispatchUpdatesTo(this)
    }

    class ProductDiffCallback(
        private val oldList: List<Followers>,
        private val newList: List<Followers>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].follower.pk == newList[newItemPosition].follower.pk

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
