package com.hk.ijournal.features.search.views

import androidx.recyclerview.widget.LinearLayoutManager
import com.hk.ijournal.common.decoration.VerticalItemDecoration
import com.hk.ijournal.databinding.FragSearchResultsBinding
import com.hk.ijournal.features.search.adapters.SearchAdapter
import com.hk.ijournal.features.search.adapters.binders.SearchResultsBinder
import com.hk.ijournal.features.search.viewmodels.SearchResultsVM
import dagger.hilt.android.AndroidEntryPoint
import omni.platform.android.components.android.BaseBindingFragment

@AndroidEntryPoint
class SearchResultsFragment: BaseBindingFragment<FragSearchResultsBinding, SearchResultsVM>() {

    private lateinit var searchAdapter: SearchAdapter

    override fun getViewModelClass(): Class<SearchResultsVM>? {
        return SearchResultsVM::class.java
    }

    override fun getViewBinding(): FragSearchResultsBinding {
        return FragSearchResultsBinding.inflate(layoutInflater)
    }

    override fun setUpViews() {
        super.setUpViews()
        initAdapter()
    }

    private fun initAdapter() {
        searchAdapter = SearchAdapter(SearchResultsBinder{
            // TODO open the page of the search result
        })
        with(binding) {
            searchResultsRv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(VerticalItemDecoration(30))
                adapter = searchAdapter
            }
        }
    }

    override fun observeData() {
        super.observeData()
        viewModel.pageResults.observe(viewLifecycleOwner) {
            searchAdapter.setItems(it)
        }
    }

    fun performSearch(uid: Long, searchQuery: String) {
        if (searchQuery.isEmpty())
            viewModel.clearResults()
        else
            viewModel.getResults(uid, searchQuery)
    }
}