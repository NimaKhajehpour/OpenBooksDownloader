package com.nima.openbooksdownloader.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.nima.openbooksdownloader.components.RecentBooksItem
import com.nima.openbooksdownloader.model.recent.RecentBooks
import com.nima.openbooksdownloader.navigation.Screens
import com.nima.openbooksdownloader.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

    val recentBooks = produceState<RecentBooks?>(initialValue = null){
        value = viewModel.getRecentBooks()
    }.value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        when (recentBooks) {
            null -> {
                CircularProgressIndicator(modifier = Modifier
                    .padding(top = 32.dp)
                    .size(32.dp))
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 13.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(items = recentBooks.books) {
                        RecentBooksItem(book = it){ id ->
                            navController.navigate(Screens.BookScreen.name+"/$id")
                        }
                    }
                }
            }
        }
    }
}