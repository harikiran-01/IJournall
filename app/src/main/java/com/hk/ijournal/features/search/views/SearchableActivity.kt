package com.hk.ijournal.features.search.views

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.SearchView
import com.hk.ijournal.databinding.ActivitySearchableBinding
import dagger.hilt.android.AndroidEntryPoint
import omni.platform.android.components.android.BaseActivityConfig

@AndroidEntryPoint
class SearchableActivity : omni.platform.android.components.android.BaseActivity<ActivitySearchableBinding, Nothing>() {
    private var userId = 0L

    override val config: BaseActivityConfig by SearchActivityConfig()

    override fun onLifecycleStart() {
        super.onLifecycleStart()
        receiveUserId()
        receiveSearchQuery(intent)
    }

    private fun receiveUserId() {
        // Verify the action and get the query
        if (Intent.ACTION_SEARCH != intent.action) {
            userId = intent.getLongExtra("uid", 0L)
            Log.d("SEADEB", userId.toString())
        }
    }

    override fun setupView() {
        super.setupView()
        initSearchView()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { receiveSearchQuery(it) }
    }

    private fun initSearchView() {
        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchView.apply {
            // Assumes current activity is the searchable activity
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
        }
        observeSearchView()
    }

    private fun observeSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                performSearch(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
    }

    private fun receiveSearchQuery(intent: Intent) {
        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                performSearch(query)
            }
        }
    }

    private fun performSearch(query: String) {
        binding.searchResults.getFragment<SearchResultsFragment>().performSearch(userId, query)
    }

    override fun getViewModelClass(): Class<Nothing>? {
        return null
    }

    override fun getViewBinding(): ActivitySearchableBinding {
        return ActivitySearchableBinding.inflate(layoutInflater)
    }

}