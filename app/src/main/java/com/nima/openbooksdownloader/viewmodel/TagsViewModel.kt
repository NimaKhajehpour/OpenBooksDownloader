package com.nima.openbooksdownloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.openbooksdownloader.database.Tag
import com.nima.openbooksdownloader.repository.OpenBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(private val repository: OpenBookRepository)
    :ViewModel(){

    fun getAllTags() = repository.getAllTags().distinctUntilChanged()

    fun addTag(tag: Tag) = viewModelScope.launch(Dispatchers.IO) {
        repository.addTag(tag)
    }
}