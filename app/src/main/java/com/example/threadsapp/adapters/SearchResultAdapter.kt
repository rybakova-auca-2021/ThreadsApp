package com.example.threadsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.threadsapp.R
import com.example.threadsapp.model.SearchResult

class SearchResultAdapter(private val results: List<SearchResult>) :
    RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val followerNameTextView = itemView.findViewById<TextView>(R.id.followerName)
        val jobTextView = itemView.findViewById<TextView>(R.id.job)
        val followersTextView = itemView.findViewById<TextView>(R.id.num_of_followers)
        val followButton = itemView.findViewById<Button>(R.id.followButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_card, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]

        holder.followerNameTextView?.text = result.username
        holder.jobTextView?.text = result.job
        holder.followersTextView?.text = result.followers + " followers"
    }

    override fun getItemCount(): Int {
        return results.size
    }
}
