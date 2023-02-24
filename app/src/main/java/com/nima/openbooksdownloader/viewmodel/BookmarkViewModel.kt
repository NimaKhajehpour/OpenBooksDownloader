package com.nima.openbooksdownloader.viewmodel

import androidx.lifecycle.ViewModel
import com.nima.openbooksdownloader.repository.OpenBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(private val repository: OpenBookRepository)
    :ViewModel(){

    fun getSavedBooks() =
        repository.getSavedBooks().distinctUntilChanged()
}