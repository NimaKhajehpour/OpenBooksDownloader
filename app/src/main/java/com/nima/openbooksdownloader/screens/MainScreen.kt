package com.nima.openbooksdownloader.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.nima.openbooksdownloader.AppDataStore
import com.nima.openbooksdownloader.R
import com.nima.openbooksdownloader.components.RecentBooksItem
import com.nima.openbooksdownloader.database.Tag
import com.nima.openbooksdownloader.navigation.OpenBooksNavigation
import com.nima.openbooksdownloader.navigation.Screens
import com.nima.openbooksdownloader.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel
) {

    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val context = LocalContext.current

    val appDataStore = AppDataStore(context)
    val darkTheme = appDataStore.getTheme.collectAsState(true).value

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var selectedDrawer by remember { mutableStateOf<String?>(null) }
    var selectedTag by remember { mutableStateOf<String?>(null) }

    var tagName by remember {
        mutableStateOf("")
    }

    val tags = viewModel.getAllTags().collectAsState(initial = emptyList()).value


    var showTagDialog by remember {
        mutableStateOf(false)
    }

    // Listen for navigation changes and update currentRoute
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            selectedDrawer = backStackEntry.destination.route
            if (backStackEntry.arguments != null){
                selectedTag = backStackEntry.arguments!!.getString("tag")
            }
        }
    }

    ModalNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
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
                            Text(text = "Dark Theme")
                        },
                        badge = {
                            Switch(
                                checked = darkTheme!!,
                                onCheckedChange = {
                                    scope.launch {
                                        appDataStore.saveTheme(!darkTheme)
                                    }
                                }
                            )
                        },
                        selected = false,
                        onClick = {
                            scope.launch {
                                appDataStore.saveTheme(!darkTheme!!)
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedIconColor = MaterialTheme.colorScheme.tertiary
                        ),
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
                        selected = selectedDrawer == Screens.DownloadsScreen.name,
                        onClick = {
                            // go to downloads
                            if (selectedDrawer != Screens.DownloadsScreen.name){
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigate(Screens.DownloadsScreen.name)
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedIconColor = MaterialTheme.colorScheme.tertiary
                        ),
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
                        selected = selectedDrawer == Screens.BookmarkScreen.name,
                        onClick = {
                            // go to bookmark
                            if (selectedDrawer != Screens.BookmarkScreen.name){
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigate(Screens.BookmarkScreen.name)
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedIconColor = MaterialTheme.colorScheme.tertiary
                        ),
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
                        selected = selectedDrawer == Screens.SearchScreen.name,
                        onClick = {
                            // go to search
                            if (selectedDrawer != Screens.SearchScreen.name){
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigate(Screens.SearchScreen.name)
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedIconColor = MaterialTheme.colorScheme.tertiary
                        ),
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
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedBadgeColor = MaterialTheme.colorScheme.tertiary
                        ),
                    )
                    Divider(modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )

                    NavigationDrawerItem(label = {
                        Text(text = "Tags")
                    }, selected = selectedDrawer == Screens.TagsScreen.name,
                        onClick = {
                            // go to tags
                            if (selectedDrawer != Screens.TagsScreen.name){
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigate(Screens.TagsScreen.name)
                            }
                        },
                        badge = {
                            IconButton(onClick = {
                                // add tags
                                scope.launch {
                                    drawerState.close()
                                }
                                showTagDialog = true
                            },
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedBadgeColor = MaterialTheme.colorScheme.tertiary
                        ),
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    if (tags.isNotEmpty()){
                        tags.forEach {
                            NavigationDrawerItem(label = {
                                Text(text = it.name)
                            }, selected = selectedDrawer == Screens.TagScreen.name+"/{tag}" && selectedTag != null && selectedTag!! == it.name,
                                onClick = {
                                    // go to tag name
                                    if (selectedDrawer != Screens.TagScreen.name+"/{tag}" || selectedTag == null || selectedTag!! != it.name){
                                        scope.launch {
                                            drawerState.close()
                                        }
                                        navController.navigate(Screens.TagScreen.name+"/${it.name}")
                                    }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedTextColor = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            )
                        }
                    }
                    NavigationDrawerItem(
                        label = {
                            Text(text = "About")
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null
                            )
                        },
                        selected = selectedDrawer == Screens.AboutScreen.name,
                        onClick = {
                            if (selectedDrawer != Screens.AboutScreen.name){
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigate(Screens.AboutScreen.name)
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedBadgeColor = MaterialTheme.colorScheme.tertiary
                        ),
                    )
                    NavigationDrawerItem(
                        label = {
                            Text(text = "Donate")
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null
                            )
                        },
                        selected = selectedDrawer == Screens.DonateScreen.name,
                        onClick = {
                            // go to Link
                            if (selectedDrawer != Screens.DonateScreen.name){
                                scope.launch {
                                    drawerState.close()
                                }
                                navController.navigate(Screens.DonateScreen.name)
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedBadgeColor = MaterialTheme.colorScheme.tertiary
                        ),
                    )
                }
            }
        },
        content = {
            Scaffold (
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        title = {
                            Text("OpenBooksDownloader")
                        },
                        navigationIcon = {
                            IconButton (onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            },
                                modifier = Modifier
                                    .padding(end = 5.dp),
                            ) {
                                AnimatedContent(targetState = drawerState){
                                    if (it.isOpen){
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
                    )
                }
            ){ padding ->
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
                Surface(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxWidth()
                ) {
                    OpenBooksNavigation(navController)
                }
            }
        }
    )
}