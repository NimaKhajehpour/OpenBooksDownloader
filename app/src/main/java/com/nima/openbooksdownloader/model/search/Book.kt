package com.nima.openbooksdownloader.model.search

data class Book(
    val authors: String,
    val id: String,
    val image: String,
    val subtitle: String,
    val title: String,
    val url: String
)