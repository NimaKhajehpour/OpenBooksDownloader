package com.nima.openbooksdownloader.network

import com.nima.openbooksdownloader.model.book.Book
import com.nima.openbooksdownloader.model.recent.RecentBooks
import com.nima.openbooksdownloader.model.search.SearchResult
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming
import retrofit2.http.Url


interface OpenBooksAPI {

    @GET("recent")
    suspend fun getRecentBooks(): RecentBooks

    @GET("search/{query}")
    suspend fun getSearchResult(@Path("query") query: String): SearchResult

    @GET("book/{id}")
    suspend fun getBook(@Path("id") id: Long): Book

    @Streaming
    @GET
    suspend fun downloadBook(@Url fileUrl: String): ResponseBody
}