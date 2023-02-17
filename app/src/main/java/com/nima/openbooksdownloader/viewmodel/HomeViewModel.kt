package com.nima.openbooksdownloader.viewmodel

import androidx.lifecycle.ViewModel
import com.nima.openbooksdownloader.network.OpenBooksAPI
import com.nima.openbooksdownloader.repository.OpenBookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: OpenBookRepository)
    :ViewModel(){

        suspend fun getRecentBooks() = repository.getRecentBooks()
    }