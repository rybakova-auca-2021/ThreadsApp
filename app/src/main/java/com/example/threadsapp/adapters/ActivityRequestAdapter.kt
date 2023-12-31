package com.example.threadsapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.ActivityRequestViewBinding
import com.example.threadsapp.model.HomeModel.Notification
import com.example.threadsapp.util.CalculateTime
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class ActivityRequestAdapter(private var requests: List<Notification>, private val viewModel: SomeoneProfileViewModel) :
    RecyclerView.Adapter<ActivityRequestAdapter.FollowerViewHolder>() {

    var setOnItemClickListener: OnItemClickListener<Notification>? = null

    interface OnItemClickListener<T> {
        fun onConfirmClick(data: T, position: Int, id: Int)
        fun onHideClick(data: T, position: Int, id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val binding =
            ActivityRequestViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val request = requests[position]
        holder.bind(request)
    }

    override fun getItemCount(): Int {
        return requests.size
    }

    inner class FollowerViewHolder(private val binding: ActivityRequestViewBinding) :
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
                    },
                    onError = {

                    })
            }
            binding.confirmBtn.setOnClickListener {
                setOnItemClickListener?.onConfirmClick(follower, adapterPosition, follower.pk)
            }
            binding.hideBtn.setOnClickListener {
                setOnItemClickListener?.onHideClick(follower, adapterPosition, follower.pk)
            }
        }
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < requests.size) {
            val updatedList = requests.toMutableList()
            updatedList.removeAt(position)
            requests = updatedList
            notifyItemRemoved(position)
        }
    }

    fun updateData(newList: List<Notification>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                requests,
                newList
            )
        )
        requests = newList
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
