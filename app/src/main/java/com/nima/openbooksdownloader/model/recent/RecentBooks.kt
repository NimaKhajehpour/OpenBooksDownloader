package com.nima.openbooksdownloader.model.recent

data class RecentBooks(
    val books: List<Book>,
    val status: String,
    val total: Int
)