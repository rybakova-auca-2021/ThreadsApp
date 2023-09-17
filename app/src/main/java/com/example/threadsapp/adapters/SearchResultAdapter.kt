package com.example.threadsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.SearchCardBinding
import com.example.threadsapp.model.UserResult
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class SearchResultAdapter(
    private var results: List<UserResult>,
    private val viewModel: SomeoneProfileViewModel,
    private var itemClickListener: OnItemClickListener? = null

) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(userResult: UserResult)
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

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val userResult = results[position]
                    itemClickListener?.onItemClick(userResult)
                }
            }
        }
        fun bind(result: UserResult) {
            viewModel.getUserProfile(
                result.username,
                onSuccess = { userProfile ->
                    with(binding) {
                        username.text = result.username
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
                onError = { errorMessage ->
                    // Handle error (e.g., show an error message)
                }
            )
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
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
