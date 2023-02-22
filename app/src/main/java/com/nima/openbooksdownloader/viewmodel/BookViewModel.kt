package com.nima.openbooksdownloader.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.openbooksdownloader.repository.OpenBookRepository
import com.nima.openbooksdownloader.utils.DownloadState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(private val repository: OpenBookRepository)
    :ViewModel(){

    suspend fun getBook(id: String) = repository.getBook(id)

    suspend fun downloadBook(url: String, destination: String) =
        viewModelScope.produce<Flow<DownloadState>> {
            trySend(repository.downloadBook(url, destination))
        }

    suspend fun getPublisherBooks(publisher: String) =
        repository.getSearchResult(query = publisher)
}