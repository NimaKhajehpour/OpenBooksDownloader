package com.nima.openbooksdownloader.model.recent

data class Book(
    val authors: String,
    val id: String,
    val image: String,
    val subtitle: String,
    val title: String,
    val url: String
)