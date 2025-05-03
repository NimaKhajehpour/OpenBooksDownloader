package com.nima.openbooksdownloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.openbooksdownloader.database.Tag
import com.nima.openbooksdownloader.repository.OpenBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class TagsViewModel (private val repository: OpenBookRepository)
    :ViewModel(){

    fun getAllTags() = repository.getAllTags().distinctUntilChanged()

    fun addTag(tag: Tag) = viewModelScope.launch(Dispatchers.IO) {
        repository.addTag(tag)
    }
}