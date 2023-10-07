package com.example.threadsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.model.ProfileModel.Followers

class FollowerAdapter(private var followers: List<Followers>) :
    RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {

    var setOnItemClickListener: OnItemClickListener<Followers>? = null

    interface OnItemClickListener<T> {
        fun onBtnClick(data: T, position: Int, id: Int)
        fun onItemClick(data: T, position: Int, id: Int, isFollowed: String)
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
        if (follower.follower.photo != null) {
            Glide.with(holder.followerPhotoImageView).load(follower.follower.photo).circleCrop()
                .into(holder.followerPhotoImageView)
        } else {
            Glide.with(holder.followerPhotoImageView).load(R.drawable.profile_photo).circleCrop()
                .into(holder.followerPhotoImageView)
        }

        val initialText = if (follower.is_followed == "Followed") "Following" else "Follow"
        holder.updateFollowButtonState(initialText)

        holder.followButton.setOnClickListener {
            setOnItemClickListener?.onBtnClick(follower, position, follower.follower.pk)
            val newText = if (holder.followButton.text == "Follow") "Following" else "Follow"
            holder.updateFollowButtonState(newText)
        }
        holder.itemView.setOnClickListener {
            setOnItemClickListener?.onItemClick(follower, position, follower.follower.pk, follower.is_followed)
        }
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

    fun removeItem(position: Int) {
        if (position >= 0 && position < followers.size) {
            val updatedList = followers.toMutableList()
            updatedList.removeAt(position)
            followers = updatedList
            notifyItemRemoved(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val followerPhotoImageView = itemView.findViewById<ImageView>(R.id.imageView)
        val followerNameTextView = itemView.findViewById<TextView>(R.id.followerName)
        val nameTextView = itemView.findViewById<TextView>(R.id.job)
        val followButton = itemView.findViewById<Button>(R.id.followButton)

        fun updateFollowButtonState(text: String) {
            followButton.text = text
            val colorResId =
                if (text == "Following") R.color.grey_auth else R.color.black
            followButton.setTextColor(
                ContextCompat.getColor(followButton.context, colorResId)
            )
        }
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
