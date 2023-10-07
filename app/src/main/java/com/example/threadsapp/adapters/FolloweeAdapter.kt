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
import com.example.threadsapp.model.ProfileModel.Followee
import com.example.threadsapp.model.ProfileModel.Followers
import com.example.threadsapp.model.ProfileModel.Follows

class FolloweeAdapter(private var followees: List<Follows>) :
    RecyclerView.Adapter<FolloweeAdapter.ViewHolder>() {

    var setOnItemClickListener: OnItemClickListener<Follows>? = null

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
        val followee = followees[position]

        holder.followeeNameTextView.text = followee.followee.username
        holder.nameTextView.text = followee.followee.full_name
        if (followee.followee.photo != null) {
            Glide.with(holder.followeePhotoImageView).load(followee.followee.photo).circleCrop()
                .into(holder.followeePhotoImageView)
        } else {
            Glide.with(holder.followeePhotoImageView).load(R.drawable.profile_photo).circleCrop()
                .into(holder.followeePhotoImageView)
        }

        val initialText = if (followee.is_followed == "Followed") "Following" else "Follow"
        holder.updateFollowButtonState(initialText)

        holder.followButton.setOnClickListener {
            setOnItemClickListener?.onBtnClick(followee, position, followee.followee.pk)
            val newText = if (holder.followButton.text == "Follow") "Following" else "Follow"
            holder.updateFollowButtonState(newText)
        }
        holder.itemView.setOnClickListener {
            setOnItemClickListener?.onItemClick(followee, position, followee.followee.pk, followee.is_followed)
        }
    }

    override fun getItemCount(): Int {
        return followees.size
    }

    fun updateData(newList: List<Follows>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                followees,
                newList
            )
        )
        followees = newList
        diffResult.dispatchUpdatesTo(this)
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < followees.size) {
            val updatedList = followees.toMutableList()
            updatedList.removeAt(position)
            followees = updatedList
            notifyItemRemoved(position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val followeePhotoImageView = itemView.findViewById<ImageView>(R.id.imageView)
        val followeeNameTextView = itemView.findViewById<TextView>(R.id.followerName)
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
        private val oldList: List<Follows>,
        private val newList: List<Follows>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].followee.pk == newList[newItemPosition].followee.pk

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
