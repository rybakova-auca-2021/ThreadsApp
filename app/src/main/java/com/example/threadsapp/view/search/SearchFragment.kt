package com.example.threadsapp.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.threadsapp.adapters.SearchResultAdapter
import com.example.threadsapp.databinding.FragmentSearchBinding
import com.example.threadsapp.model.SearchResult

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerViewSearch
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultsList = getResultList()

        val adapter = SearchResultAdapter(resultsList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun getResultList(): List<SearchResult> {
        return listOf(
            SearchResult("iamnalimov", "UX/UI", "45"),
            SearchResult("lily.rose", "Web developer", "47474"),
            SearchResult("lana.hope", "Mobile developer", "23")
        )
    }
}