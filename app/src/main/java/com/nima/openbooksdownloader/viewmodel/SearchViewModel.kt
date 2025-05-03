package com.nima.openbooksdownloader.viewmodel

import androidx.lifecycle.ViewModel
import com.nima.openbooksdownloader.repository.OpenBookRepository

class SearchViewModel (private val repository: OpenBookRepository)
    :ViewModel(){

        suspend fun getSearchResult(query: String) = repository.getSearchResult(query)
}