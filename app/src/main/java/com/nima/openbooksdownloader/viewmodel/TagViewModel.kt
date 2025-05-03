package com.nima.openbooksdownloader.viewmodel

import androidx.lifecycle.ViewModel
import com.nima.openbooksdownloader.repository.OpenBookRepository
import kotlinx.coroutines.flow.distinctUntilChanged

class TagViewModel (private val repository: OpenBookRepository)
    :ViewModel(){

    fun getBooksByTag(tag: String) =
        repository.getBookByTag(tag).distinctUntilChanged()
}