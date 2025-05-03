package com.nima.openbooksdownloader.viewmodel

import androidx.lifecycle.ViewModel
import com.nima.openbooksdownloader.repository.OpenBookRepository
import kotlinx.coroutines.flow.distinctUntilChanged

class DownloadsViewModel(private val repository: OpenBookRepository)
    : ViewModel(){

    fun getBooks() =
        repository.getSavedBooks().distinctUntilChanged()
}