package com.nima.openbooksdownloader.viewmodel

import androidx.lifecycle.ViewModel
import com.nima.openbooksdownloader.repository.OpenBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class DownloadsViewModel @Inject constructor(private val repository: OpenBookRepository)
    : ViewModel(){

    fun getBooks() =
        repository.getSavedBooks().distinctUntilChanged()
}