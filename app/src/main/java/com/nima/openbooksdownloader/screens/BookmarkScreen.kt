package com.nima.openbooksdownloader.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.openbooksdownloader.components.BookmarkItem
import com.nima.openbooksdownloader.viewmodel.BookmarkViewModel

@Composable
fun BookmarkScreen (
    navController: NavController,
    viewModel: BookmarkViewModel
){

    val savedBooks = viewModel.getSavedBooks().collectAsState(initial = emptyList()).value

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        items(items = savedBooks){
            BookmarkItem(book = it) {
                // go to bookmarked book
            }
        }
    }
}