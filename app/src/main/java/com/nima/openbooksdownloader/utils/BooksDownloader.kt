package com.nima.openbooksdownloader.utils

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import java.io.File

object BookDownloader {

    fun downloadBook(
        responseBody: ResponseBody,
        context: Context,
        fileName: String
    ): Flow<DownloadState> = flow {
        emit(DownloadState.Downloading(0))

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val destinationFile = File(downloadsDir, "$fileName.pdf")

        try {
            responseBody.byteStream().use { input ->
                destinationFile.outputStream().use { output ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    val totalBytes = responseBody.contentLength()
                    var downloadedBytes = 0L
                    var read = input.read(buffer)

                    while (read >= 0) {
                        output.write(buffer, 0, read)
                        downloadedBytes += read
                        emit(DownloadState.Downloading(((downloadedBytes * 100) / totalBytes).toInt()))
                        read = input.read(buffer)
                    }
                }
            }
            emit(DownloadState.Finished)
        } catch (e: Exception) {
            emit(DownloadState.Failed(e))
        }
    }.flowOn(Dispatchers.IO).distinctUntilChanged()
}
