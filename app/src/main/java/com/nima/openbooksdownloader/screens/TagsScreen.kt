package com.nima.openbooksdownloader.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.openbooksdownloader.components.TagItem
import com.nima.openbooksdownloader.database.Tag
import com.nima.openbooksdownloader.navigation.Screens
import com.nima.openbooksdownloader.viewmodel.TagsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TagsScreen(
    navController: NavController,
    viewModel: TagsViewModel
) {

    var showAddTagDialog by remember {
        mutableStateOf(false)
    }

    var tagName by remember {
        mutableStateOf("")
    }

    val allTags = viewModel.getAllTags().collectAsState(initial = emptyList()).value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (showAddTagDialog){
            AlertDialog(
                onDismissRequest = {
                    showAddTagDialog = false
                    tagName = ""
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val tag = Tag(tagName.trim())
                            viewModel.addTag(tag)
                            tagName = ""
                            showAddTagDialog = false
                        },
                        enabled = tagName.isNotBlank() && tagName.trim().length <= 20
                    ) {
                        Text(text = "Add Tag")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showAddTagDialog = false
                            tagName = ""
                        },
                    ) {
                        Text(text = "Cancel")
                    }
                },
                title = {
                    Text(text = "Add Tag")
                },
                text = {
                    OutlinedTextField(value = tagName,
                        onValueChange = {
                            tagName = it
                        },
                        singleLine = true,
                        isError = tagName.trim().length > 20,
                        supportingText = {
                            Text(text = "Tag can be up to 20 characters in length")
                        }
                    )
                },
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            stickyHeader {
                Surface(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 5.dp),
                    shape = RoundedCornerShape(5.dp),
                    shadowElevation = 10.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "All Tags")
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = {
                            showAddTagDialog = true
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                        }
                    }
                }

            }

            items(items = allTags){
                TagItem(tag = it, onClick = {
                    // go to tag
                    navController.navigate(Screens.TagScreen.name+"/${it.name}")
                })
            }
        }
    }
}