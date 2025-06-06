package com.nima.openbooksdownloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nima.openbooksdownloader.database.Book
import com.nima.openbooksdownloader.repository.OpenBookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class SavedBookViewModel (private val repository: OpenBookRepository)
    : ViewModel(){

    fun getBookById(id: String) =
        repository.getBookById(id).distinctUntilChanged()

    fun updateBook(book: Book) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBook(book)
        }

    fun deleteBook(book: Book) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBook(book)
        }

    fun getAllTags() =
        repository.getAllTags().distinctUntilChanged()
}