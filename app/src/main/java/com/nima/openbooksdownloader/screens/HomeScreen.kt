package com.nima.openbooksdownloader.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

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

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()
    
    BackHandler(drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ){
                    Spacer(modifier = Modifier.height(12.dp))

                    NavigationDrawerItem(
                        label = {
                            Text(text = "Close")
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = {
                            Text(text = "Downloads")
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_download_24),
                                contentDescription = null
                            )
                        },
                        selected = false,
                        onClick = {
                            // go to downloads
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)

                    )
                    NavigationDrawerItem(
                        label = {
                            Text(text = "Bookmarks")
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_bookmark_border_24),
                                contentDescription = null
                            )
                        },
                        selected = false,
                        onClick = {
                            // go to bookmark
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),

                        )
                    NavigationDrawerItem(
                        label = {
                            Text(text = "Search")
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        },
                        selected = false,
                        onClick = {
                            // go to search
                            navController.navigate(Screens.SearchScreen.name)
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)

                    )
                    Divider(modifier = Modifier.padding(16.dp))

                    NavigationDrawerItem(label = {
                        Text(text = "Tags")
                    }, selected = false,
                        onClick = {
                            // go to tags
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        badge = {
                            IconButton(onClick = {
                                // add tags
                                scope.launch {
                                    drawerState.close()
                                }
                            }) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                    }
                }
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
    )
}