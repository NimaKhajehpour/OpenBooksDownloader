package com.nima.openbooksdownloader.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.nima.openbooksdownloader.model.book.Book
import com.nima.openbooksdownloader.utils.DownloadState
import com.nima.openbooksdownloader.viewmodel.BookViewModel
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun BookScreen (
    navController: NavController,
    viewModel: BookViewModel,
    id: String?
){

    val book = produceState<Book?>(initialValue = null){
        value = viewModel.getBook(id!!.toLong())
    }.value

    var destination by remember{
        mutableStateOf("")
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()){}

    LaunchedEffect(key1 = destination){
        if (destination.isNotBlank()){
            launch {
                viewModel.downloadBook(book!!.download, destination).receive().collect{
                    val state = when(it){
                        is DownloadState.Downloading -> {
                        }
                        is DownloadState.Failed -> {
                            Log.d("LOL", "BookScreen: ${it.error?.localizedMessage}")
                        }
                        is DownloadState.Finished -> {
                        }
                    }
                }
                destination = ""
            }
        }
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (book == null){
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }else{
            Button(onClick = {
                when(PackageManager.PERMISSION_GRANTED){
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .and(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)) -> {
                        destination = book.title
                    }
                    else -> {
                        permissionLauncher.launch(arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                        ))
                    }
                }
            }) {
                Text(text = "Download")
            }
        }
    }
}