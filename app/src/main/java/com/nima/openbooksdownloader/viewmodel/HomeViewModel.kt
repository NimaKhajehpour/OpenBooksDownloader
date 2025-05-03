package com.nima.openbooksdownloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.openbooksdownloader.database.Book
import com.nima.openbooksdownloader.database.BookDao
import com.nima.openbooksdownloader.database.Tag
import com.nima.openbooksdownloader.network.OpenBooksAPI
import com.nima.openbooksdownloader.repository.OpenBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.File

class HomeViewModel
    (private val repository: OpenBookRepository)
    :ViewModel(){

    suspend fun getRecentBooks() = repository.getRecentBooks()

    fun getAllTags() = repository.getAllTags().distinctUntilChanged()

    fun addTag(tag: Tag) = viewModelScope.launch(Dispatchers.IO){ repository.addTag(tag) }


}