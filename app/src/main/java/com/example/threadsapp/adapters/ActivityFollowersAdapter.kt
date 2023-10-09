package com.example.threadsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.ActivityFollowerViewBinding
import com.example.threadsapp.model.HomeModel.Notification
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class ActivityFollowersAdapter(var followers: List<Notification>, private val viewModel: SomeoneProfileViewModel) :
    RecyclerView.Adapter<ActivityFollowersAdapter.FollowerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val binding =
            ActivityFollowerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val follower = followers[position]
        holder.bind(follower)
    }

    override fun getItemCount(): Int {
        return followers.size
    }

    inner class FollowerViewHolder(private val binding: ActivityFollowerViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(follower: Notification) {
            follower.related_user?.let {
                viewModel.getUserProfileById(
                    it,
                    onSuccess = { userProfile ->
                        binding.followerName.text = userProfile.username
                        userProfile.photo.let { photoUrl ->
                            if (photoUrl.isNullOrEmpty()) {
                                Glide.with(binding.root.context).load(R.drawable.profile_photo)
                                    .into(binding.avatar)
                            } else {
                                Glide.with(binding.root.context).load(photoUrl).circleCrop()
                                    .into(binding.avatar)
                            }
                        }
                        binding.text.text = follower.text
                        binding.time.text = follower.date_posted
                    },
                    onError = {

                    })
            }
        }
    }
    fun updateData(newList: List<Notification>) {
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
