package com.example.threadsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.threadsapp.databinding.SearchCardBinding
import com.example.threadsapp.model.UserResult

class SearchResultAdapter(private var results: List<UserResult>) :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

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
        fun bind(result: UserResult) {
            with(binding) {
                username.text = result.username
            }
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
