package com.nima.openbooksdownloader.model.search

data class SearchResult(
    val books: List<Book>,
    val status: String,
    val total: String
)