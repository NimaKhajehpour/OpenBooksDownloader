package com.nima.openbooksdownloader.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.nima.openbooksdownloader.R
import com.nima.openbooksdownloader.components.RecentBooksItem
import com.nima.openbooksdownloader.database.Tag
import com.nima.openbooksdownloader.model.recent.RecentBooks
import com.nima.openbooksdownloader.navigation.Screens
import com.nima.openbooksdownloader.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
) {

    val tags = viewModel.getAllTags().collectAsState(initial = emptyList()).value

    val recentBooks = produceState<RecentBooks?>(initialValue = null){
        value = viewModel.getRecentBooks()
    }.value

    var showTagDialog by remember {
        mutableStateOf(false)
    }

    var tagName by remember {
        mutableStateOf("")
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scope = rememberCoroutineScope()
    
    BackHandler(drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    val context = LocalContext.current

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
                            navController.navigate(Screens.DownloadsScreen.name)
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
                            navController.navigate(Screens.BookmarkScreen.name)
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
                    NavigationDrawerItem(
                        label = {
                            Text(text = "Powered By: dbooks.org")
                        },
                        badge = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_link_24),
                                contentDescription = null
                            )
                        },
                        selected = false,
                        onClick = {
                            // go to Link
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.dbooks.org/"))
                            context.startActivity(intent)
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
                            navController.navigate(Screens.TagsScreen.name)
                        },
                        badge = {
                            IconButton(onClick = {
                                // add tags
                                scope.launch {
                                    drawerState.close()
                                }
                                showTagDialog = true
                            }) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    if (tags.isNotEmpty()){
                        tags.forEach {
                            NavigationDrawerItem(label = {
                                Text(text = it.name)
                            }, selected = false,
                                onClick = {
                                    // go to tag name
                                    scope.launch {
                                        drawerState.close()
                                    }
                                    navController.navigate(Screens.TagScreen.name+"/${it.name}")
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }
                }
            }
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ){

                if (showTagDialog){
                    AlertDialog(
                        onDismissRequest = {
                            showTagDialog = false
                            tagName = ""
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val tag = Tag(tagName.trim())
                                    viewModel.addTag(tag)
                                    tagName = ""
                                    showTagDialog = false
                                },
                                enabled = tagName.isNotBlank() && tagName.trim().length <= 20
                            ) {
                                Text(text = "Add Tag")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showTagDialog = false
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

                when (recentBooks) {
                    null -> {
                        CircularProgressIndicator(modifier = Modifier
                            .padding(top = 32.dp)
                            .size(32.dp)
                            .align(Alignment.TopCenter))
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
                SmallFloatingActionButton(onClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 16.dp, end = 8.dp),
                    shape = RoundedCornerShape(
                        topEndPercent =
                            animateIntAsState(targetValue = if (drawerState.isOpen) 50 else 10).value,
                        topStartPercent =
                        animateIntAsState(targetValue = if (drawerState.isOpen) 50 else 10).value,
                        bottomEndPercent =
                        animateIntAsState(targetValue = if (drawerState.isOpen) 50 else 10).value,
                        bottomStartPercent =
                        animateIntAsState(targetValue = if (drawerState.isOpen) 50 else 10).value
                    )
                ) {
                    AnimatedContent(targetState = drawerState){
                        if (drawerState.isOpen){
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }else{
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    )
}