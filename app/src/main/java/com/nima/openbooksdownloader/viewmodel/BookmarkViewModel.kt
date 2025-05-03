package com.nima.openbooksdownloader.viewmodel

import androidx.lifecycle.ViewModel
import com.nima.openbooksdownloader.repository.OpenBookRepository
import kotlinx.coroutines.flow.distinctUntilChanged


class BookmarkViewModel (private val repository: OpenBookRepository)
    :ViewModel(){

    fun getSavedBooks() =
        repository.getSavedBooks().distinctUntilChanged()
}