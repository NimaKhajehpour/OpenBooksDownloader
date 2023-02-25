package com.nima.openbooksdownloader.screens

import android.content.Intent
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nima.openbooksdownloader.database.Tag
import com.nima.openbooksdownloader.navigation.Screens
import com.nima.openbooksdownloader.utils.Constants
import com.nima.openbooksdownloader.viewmodel.SavedBookViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedBookScreen (
    navController: NavController,
    viewModel: SavedBookViewModel,
    id: String?
){

    val savedBook = viewModel.getBookById(id!!).collectAsState(initial = null).value

    var bookNote by remember(savedBook) {
        mutableStateOf(savedBook?.note.orEmpty())
    }

    var tagMenuExpanded by remember {
        mutableStateOf(false)
    }

    val allTags = viewModel.getAllTags().collectAsState(initial = emptyList()).value

    val context = LocalContext.current

    if (savedBook != null){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (!File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "${savedBook.title}.pdf"
                    ).isFile){
                    IconButton(onClick = {
                        viewModel.deleteBook(savedBook)
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = {
                    navController.navigate(Screens.BookScreen.name+"/$id")
                }) {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                }
            }

            Card(
                modifier = Modifier
                    .size(110.dp, 140.dp)
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(5.dp),
            ) {
                AsyncImage(model = Constants.imageBaseUrl+"${savedBook.id}.jpg",
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
            }

            if (File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "${savedBook.title}.pdf"
                ).isFile){
                Button(onClick = {
                    val openPDF =
                        Intent(Intent.ACTION_VIEW)
                    openPDF.setDataAndType(
                        FileProvider.getUriForFile(context, context.applicationContext.packageName+".provider", File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                            "${savedBook.title}.pdf")),
                        "application/pdf"
                    )
                    openPDF.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    context.startActivity(Intent.createChooser(openPDF, "Open With..."))
                }) {
                    Text(text = "Start Reading")
                }
            }

            OutlinedTextField(
                value = savedBook.title,
                onValueChange = {},
                readOnly = true,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 32.dp),
                label = {
                    Text(text = "Title")
                },
                textStyle = MaterialTheme.typography.bodyLarge,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ){
                OutlinedTextField(
                    value = bookNote,
                    onValueChange = {
                        bookNote = it
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    label = {
                        Text(text = "Notes")
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                )

                IconButton(onClick = {
                    viewModel.updateBook(savedBook.copy(note = bookNote))
                },
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                }
            }

            ExposedDropdownMenuBox(expanded = tagMenuExpanded,
                onExpandedChange = { tagMenuExpanded = tagMenuExpanded.not() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp)
            ) {
                TextField(
                    value = savedBook.tag.ifBlank { "None" },
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    onValueChange = {},
                    label = { Text("Tag") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tagMenuExpanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = tagMenuExpanded,
                    onDismissRequest = { tagMenuExpanded = false },
                ) {
                    allTags.plus(Tag("None")).forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption.name) },
                            onClick = {
                                viewModel.updateBook(
                                    savedBook.copy(tag =
                                        if (selectionOption.name != "None"){
                                            selectionOption.name
                                        }else{
                                            ""
                                        }
                                    ))
                                tagMenuExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }
    }
}