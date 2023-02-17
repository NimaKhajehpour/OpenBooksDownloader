package com.nima.openbooksdownloader.repository

import com.nima.openbooksdownloader.network.OpenBooksAPI
import com.nima.openbooksdownloader.utils.DownloadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import okhttp3.ResponseBody
import retrofit2.Callback
import java.io.File
import javax.inject.Inject

class OpenBookRepository @Inject constructor(private val api: OpenBooksAPI) {

    private fun ResponseBody.saveFile(destination: String): Flow<DownloadState>{
        return flow{
            emit(DownloadState.Downloading(0))
            val destinationFile = File(destination)

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

    suspend fun getBook(id: Int) = api.getBook(id)
}