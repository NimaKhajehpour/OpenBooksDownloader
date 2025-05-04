package com.nima.openbooksdownloader.screens

import android.content.Intent
import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.nima.openbooksdownloader.components.BookmarkItem
import com.nima.openbooksdownloader.navigation.Screens
import com.nima.openbooksdownloader.viewmodel.TagViewModel
import java.io.File

@Composable
fun TagScreen(
    navController: NavController,
    viewModel: TagViewModel,
    tag: String?
) {

    val tagBooks = viewModel.getBooksByTag(tag!!).collectAsState(initial = emptyList()).value

    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        items(items = tagBooks){
            BookmarkItem(book = it,
                onOpen = {
                    val openPDF =
                        Intent(Intent.ACTION_VIEW)
                    openPDF.setDataAndType(
                        FileProvider.getUriForFile(context, context.applicationContext.packageName+".provider", File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            "${it.title}.pdf")
                        ),
                        "application/pdf"
                    )
                    openPDF.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    context.startActivity(Intent.createChooser(openPDF, "Open With..."))
                }
            ) {
                // go to bookmarked book
                navController.navigate(Screens.SavedBookScreen.name+"/${it.id}")
            }
        }
    }
}