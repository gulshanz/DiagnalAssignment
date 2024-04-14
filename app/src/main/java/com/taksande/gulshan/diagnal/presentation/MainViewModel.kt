package com.taksande.gulshan.diagnal.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.taksande.gulshan.diagnal.data.local.dto.Content
import com.taksande.gulshan.diagnal.domain.usecases.ListBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    listBooksUseCase: ListBooksUseCase
) : ViewModel() {

    private val currentBooksList: MutableLiveData<PagingData<Content>> = MutableLiveData()
    val searchQuery: MutableLiveData<String> = MutableLiveData("")
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val allBooks: LiveData<PagingData<Content>> =
        listBooksUseCase().cachedIn(viewModelScope)

    init {
        // fetching required data according to search query
        viewModelScope.launch {
            combine(
                searchQuery.asFlow(), allBooks.asFlow()
            ) { query, books ->
                if (query.isBlank() || query.length < 3) {
                    books
                } else {
                    books.filter { it.name.contains(query, ignoreCase = true) }
                }
            }.collect { currentBooksList.value = it }
        }
    }

    fun getBooksList(): LiveData<PagingData<Content>> {
        return currentBooksList
    }

    fun isQueryBlank(): Boolean {
        searchQuery.value?.let {
            return it.isBlank()
        }
        return true
    }

    fun clearQuery() {
        searchQuery.value = ""
    }
}