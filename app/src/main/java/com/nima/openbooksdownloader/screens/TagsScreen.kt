package com.nima.openbooksdownloader.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.openbooksdownloader.components.TagItem
import com.nima.openbooksdownloader.database.Tag
import com.nima.openbooksdownloader.navigation.Screens
import com.nima.openbooksdownloader.viewmodel.TagsViewModel

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

    Scaffold (
        floatingActionButton = {
            FloatingActionButton (
                onClick = {
                    showAddTagDialog = true
                }
            ) {
                Icon(Icons.Default.Add, null)
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ){padding ->
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
                        enabled = tagName.isNotBlank() && tagName.trim().length <= 20,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.tertiary
                        )
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
            modifier = Modifier.fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            items(items = allTags){
                TagItem(tag = it, onClick = {
                    // go to tag
                    navController.navigate(Screens.TagScreen.name+"/${it.name}")
                })
            }
        }
    }
}