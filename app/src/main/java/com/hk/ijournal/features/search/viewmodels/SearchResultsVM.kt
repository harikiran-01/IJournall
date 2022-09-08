package com.hk.ijournal.features.search.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hk.ijournal.features.dayentry.models.Page
import com.hk.ijournal.features.search.usecases.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SearchResultsVM @Inject
constructor(
    private val searchUseCase: SearchUseCase
): ViewModel() {

    private val _pageResults = MutableLiveData<List<Page>>(mutableListOf())
    val pageResults: LiveData<List<Page>>
        get() = _pageResults

    fun getResults(uid: Long, searchQuery: String) = runBlocking {
        _pageResults.value = searchUseCase.getResults(uid, searchQuery)
    }

    fun clearResults() {
        _pageResults.postValue(emptyList())
    }
}