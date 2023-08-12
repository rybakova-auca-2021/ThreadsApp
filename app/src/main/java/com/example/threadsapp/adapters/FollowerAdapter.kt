package com.example.threadsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.threadsapp.R
import com.example.threadsapp.model.Follower

class FollowerAdapter(private val followers: List<Follower>) :
    RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        holder.followerNameTextView.text = follower.username
        holder.nameTextView.text = follower.name

        if (follower.isFollowing) {
            holder.followButton.text = "Following"
            holder.followButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey_auth))
        } else if (follower.isRequested) {
            holder.followButton.text = "Cancel"
            holder.followButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey_auth))
        } else {
            holder.followButton.text = "Follow"
            holder.followButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        }


        holder.followButton.setOnClickListener {
            if (follower.isFollowing) {
                follower.isFollowing = false
                holder.followButton.text = "Follow"
                holder.followButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            } else if (follower.isRequested) {
                follower.isRequested = false
                holder.followButton.text = "Follow"
                holder.followButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
            } else {
                follower.isFollowing = true
                holder.followButton.text = "Following"
                holder.followButton.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey_auth))
            }
        }
    }

    override fun getItemCount(): Int {
        return followers.size
    }
}
