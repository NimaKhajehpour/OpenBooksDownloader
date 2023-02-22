package com.nima.openbooksdownloader.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.nima.openbooksdownloader.R
import com.nima.openbooksdownloader.components.RecentBooksItem
import com.nima.openbooksdownloader.model.recent.RecentBooks
import com.nima.openbooksdownloader.navigation.Screens
import com.nima.openbooksdownloader.viewmodel.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

    val recentBooks = produceState<RecentBooks?>(initialValue = null){
        value = viewModel.getRecentBooks()
    }.value

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 5.dp
            ) {
                NavigationBarItem(selected = false, onClick = {
                    // go to downloads
                },
                    icon = {
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_download_24), contentDescription = null)

                    },
                    label = {
                        Text(text = "Downloads")
                    },
                    alwaysShowLabel = true
                )
                NavigationBarItem(selected = false, onClick = {
                    // go to bookmark
                },
                    icon = {
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_bookmark_border_24), contentDescription = null)

                    },
                    label = {
                        Text(text = "Bookmarks")
                    },
                    alwaysShowLabel = true
                )
                NavigationBarItem(selected = false, onClick = {
                    // go to Search
                },
                    icon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)

                    },
                    label = {
                        Text(text = "Search")
                    },
                    alwaysShowLabel = true
                )
            }
        },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = 72.dp),
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
}