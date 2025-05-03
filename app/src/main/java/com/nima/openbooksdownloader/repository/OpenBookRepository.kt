package com.nima.openbooksdownloader.repository

import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.nima.openbooksdownloader.database.Book
import com.nima.openbooksdownloader.database.BookDao
import com.nima.openbooksdownloader.database.Tag
import com.nima.openbooksdownloader.network.OpenBooksAPI
import com.nima.openbooksdownloader.utils.DownloadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody
import retrofit2.Callback
import java.io.File

class OpenBookRepository
    (private val api: OpenBooksAPI, private val dao: BookDao) {

    private fun ResponseBody.saveFile(destination: String): Flow<DownloadState>{
        return flow{
            emit(DownloadState.Downloading(0))
            val destinationFile =
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "$destination.pdf")

            try{
                byteStream().use { input ->
                    destinationFile.outputStream().use { output ->
                        val totalBytes = contentLength()
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var progressBytes = 0L
                        var bytes = input.read(buffer)
                        while (bytes >= 0){
                            output.write(buffer, 0, bytes)
                            progressBytes += bytes
                            bytes = input.read(buffer)
                            emit(DownloadState.Downloading(((progressBytes * 100) / totalBytes).toInt()))
                        }
                    }
                }
                emit(DownloadState.Finished)
            }catch (e: Exception){
                emit(DownloadState.Failed(e))
            }
        }.flowOn(Dispatchers.IO).distinctUntilChanged()
    }

    suspend fun downloadBook(url: String, destination: String) =
        api.downloadBook(url).saveFile(destination)

    suspend fun getRecentBooks() = api.getRecentBooks()

    suspend fun getSearchResult(query: String) = api.getSearchResult(query)

    suspend fun getBook(id: String) = api.getBook(id)

    fun getAllTags(): Flow<List<Tag>> =
        dao.getAllTags().flowOn(Dispatchers.IO).conflate()

    fun getBookByTag(tag: String): Flow<List<Book>> =
        dao.getBookByTag(tag).flowOn(Dispatchers.IO).conflate()

    fun getBookById(id: String): Flow<Book?> =
        dao.getBookById(id).flowOn(Dispatchers.IO).conflate()

    suspend fun addTag(tag: Tag) =
        dao.addTag(tag)

    suspend fun addBook(book: Book) =
        dao.addBook(book)

    suspend fun deleteBook(book: Book) =
        dao.deleteBook(book)

    suspend fun updateBook(book: Book) =
        dao.updateBook(book)

    fun getSavedBooks(): Flow<List<Book>> =
        dao.getSavedBooks().flowOn(Dispatchers.IO).conflate()

}