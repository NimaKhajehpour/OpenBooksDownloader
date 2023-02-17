package com.nima.openbooksdownloader.model.book

data class Book(
    val authors: String,
    val description: String,
    val download: String,
    val id: String,
    val image: String,
    val pages: String,
    val publisher: String,
    val status: String,
    val subtitle: String,
    val title: String,
    val url: String,
    val year: String
)