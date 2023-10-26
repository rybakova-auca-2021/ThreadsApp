package com.example.threadsapp.adapters

import android.annotation.SuppressLint
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
import com.example.threadsapp.databinding.SearchCardBinding
import com.example.threadsapp.model.UserResult
import com.example.threadsapp.viewModel.followViewModel.FollowSomeoneViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class SearchResultAdapter(
    private var results: List<UserResult>,
    private val viewModel: SomeoneProfileViewModel,

) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    var setOnItemClickListener: OnItemClickListener<UserResult>? = null

    interface OnItemClickListener<T> {
        fun onItemClick(data: T, position: Int, id: Int)
        fun onBtnClick(data: T, position: Int, id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchCardBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.bind(result)
        holder.itemView.setOnClickListener {
            setOnItemClickListener?.onItemClick(result, position, result.pk)
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun updateData(newList: List<UserResult>) {
        val diffResult = DiffUtil.calculateDiff(ProductDiffCallback(results, newList))
        results = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(private val binding: SearchCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(result: UserResult) {
            viewModel.getUserProfile(
                result.username,
                onSuccess = { userProfile ->
                    with(binding) {
                        username.text = result.username
                        numOfFollowers.text = "${userProfile.followers_count} followers"
                        job.text = userProfile.full_name
                        userProfile.photo.let { photoUrl ->
                            if (photoUrl.isNullOrEmpty()) {
                                Glide.with(binding.root.context).load(R.drawable.profile_photo).into(binding.avatar)
                            } else {
                                Glide.with(binding.root.context).load(photoUrl).circleCrop().into(binding.avatar)
                            }
                        }
                    }
                },
                onError = {
                }
            )

            val initialText = when (result.is_followed) {
                "Follow in response" -> "Requested"
                "Mutual Follow" -> "Following"
                "Followed" -> "Followed"
                else -> "Follow"
            }
            updateFollowButtonState(initialText)

            binding.followButton.setOnClickListener {
                setOnItemClickListener?.onBtnClick(result, adapterPosition, result.pk)
                val newText = when (result.is_followed) {
                    "Follow in response" -> "Follow"
                    "Mutual Follow" -> "Follow"
                    "Followed" -> "Follow"
                    else -> "Following"
                }
                updateFollowButtonState(newText)
            }
        }
        private fun updateFollowButtonState(text: String) {
            binding.followButton.text = text
            val colorResId =
                if (text == "Following" || text == "Requested") R.color.grey_auth else R.color.black
            binding.followButton.setTextColor(
                ContextCompat.getColor(binding.followButton.context, colorResId)
            )
        }
    }

    class ProductDiffCallback(
        private val oldList: List<UserResult>,
        private val newList: List<UserResult>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].pk == newList[newItemPosition].pk

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}
