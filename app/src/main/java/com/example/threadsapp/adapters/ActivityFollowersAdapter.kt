package com.example.threadsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.ActivityFollowerViewBinding
import com.example.threadsapp.model.HomeModel.Notification
import com.example.threadsapp.util.CalculateTime
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class ActivityFollowersAdapter(var followers: List<Notification>, private val viewModel: SomeoneProfileViewModel) :
    RecyclerView.Adapter<ActivityFollowersAdapter.FollowerViewHolder>() {

    var setOnItemClickListener: OnItemClickListener<Notification>? = null

    interface OnItemClickListener<T> {
        fun onBtnClick(data: T, position: Int, id: Int)
        fun onItemClick(data: T, position: Int, id: Int)
    }

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
                        val timeDifference = CalculateTime.calculateTimeDifference(follower.date_posted)
                        binding.time.text = timeDifference

                        val initialText = when (userProfile.is_followed) {
                            "Mutual Follow" -> "Following"
                            "Followed" -> "Following"
                            "Follow in response" -> "Requested"
                            else -> "Follow"
                        }
                        updateFollowButtonState(initialText)

                        binding.followButton.setOnClickListener {
                            setOnItemClickListener?.onBtnClick(follower, adapterPosition, follower.related_user)
                            val newText = when (userProfile.is_followed) {
                                "Mutual Follow" -> "Follow"
                                "Followed" -> "Follow"
                                "Follow in response" -> "Follow"
                                else -> "Following"
                            }
                            updateFollowButtonState(newText)
                        }
                        itemView.setOnClickListener {
                            setOnItemClickListener?.onItemClick(follower, adapterPosition, follower.related_user)
                        }
                    },
                    onError = {

                    }
                )
            }
        }
        private fun updateFollowButtonState(text: String) {
            binding.followButton.text = text
            val colorResId =
                if (text == "Following") R.color.grey_auth else R.color.black
            binding.followButton.setTextColor(
                ContextCompat.getColor(binding.followButton.context, colorResId)
            )
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
