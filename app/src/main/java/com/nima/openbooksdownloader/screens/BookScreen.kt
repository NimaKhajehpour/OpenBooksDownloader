package com.nima.openbooksdownloader.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.nima.openbooksdownloader.BuildConfig
import com.nima.openbooksdownloader.R
import com.nima.openbooksdownloader.model.book.Book
import com.nima.openbooksdownloader.utils.DownloadState
import com.nima.openbooksdownloader.viewmodel.BookViewModel
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen (
    navController: NavController,
    viewModel: BookViewModel,
    id: String?
){

    val book = produceState<Book?>(initialValue = null){
        value = viewModel.getBook(id!!)
    }.value

    var destination by remember{
        mutableStateOf("")
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()){}

    var downloading by remember {
        mutableStateOf(false)
    }

    var downloadProgress by remember {
        mutableStateOf(0)
    }

    var downloadFailed by remember {
        mutableStateOf(false)
    }
    var downloaded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = destination){
        if (destination.isNotBlank()){
            launch {
                viewModel.downloadBook(book!!.download, destination).receive().collect{
                    val state = when(it){
                        is DownloadState.Downloading -> {
                            downloaded = false
                            downloadFailed = false
                            downloading = true
                            downloadProgress = it.progress
                        }
                        is DownloadState.Failed -> {
                            downloaded = false
                            downloading = false
                            downloadFailed = true
                        }
                        is DownloadState.Finished -> {
                            downloadFailed = false
                            downloading = false
                            downloaded = true
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp, start = 32.dp, end = 32.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ){
                Card(
                    shape = RoundedCornerShape(5.dp),
                    elevation = CardDefaults.cardElevation(10.dp),
                ) {
                    SubcomposeAsyncImage(
                        model = book.image, contentDescription = null,
                    )
                }
                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Title: ${book.title}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = book.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "Authors: ${book.authors}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Light
                    )

                    Text(
                        text = "Publisher: ${book.publisher}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = "Year: ${book.year}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Light
                    )
                    Text(
                        text = "Pages: ${book.pages}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){

                if (downloading){
                    LinearProgressIndicator(progress = downloadProgress / 100f)
                    Text(text = "%$downloadProgress",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.ExtraLight,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                }

                if (!downloading){
                    if (!File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            "${book.title}.pdf"
                        ).isFile){
                        IconButton(
                            onClick = {
                                when (PackageManager.PERMISSION_GRANTED) {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    )
                                        .and(
                                            ContextCompat.checkSelfPermission(
                                                context,
                                                Manifest.permission.READ_EXTERNAL_STORAGE
                                            )
                                        ) -> {
                                        destination = book.title
                                    }
                                    else -> {
                                        permissionLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                            )
                                        )
                                    }
                                }
                            },
                            enabled = !File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                "${book.title}.pdf"
                            ).isFile
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_download_24),
                                contentDescription = null
                            )
                        }
                    }else{
                        Button(onClick = {
                            val openPDF =
                                Intent(Intent.ACTION_VIEW)
                            openPDF.setDataAndType(
                                FileProvider.getUriForFile(context, context.applicationContext.packageName+".provider", File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                    "${book.title}.pdf")),
                                "application/pdf"
                            )
                            openPDF.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                            context.startActivity(Intent.createChooser(openPDF, "Open With..."))
                        }) {
                            Text(text = "Start Reading")
                        }
                    }
                }
                IconButton(
                    onClick = {
                        // toggle saving book
                    },
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_bookmark_border_24),
                        contentDescription = null)
                }

            }

        }
    }
}