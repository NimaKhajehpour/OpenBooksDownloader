package com.nima.openbooksdownloader.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.openbooksdownloader.components.SearchResultBookItem
import com.nima.openbooksdownloader.model.search.SearchResult
import com.nima.openbooksdownloader.navigation.Screens
import com.nima.openbooksdownloader.viewmodel.SearchViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel
) {

    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    var searchResult: SearchResult? by remember {
        mutableStateOf(null)
    }

    var expandedSearchBar by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = searchQuery){
        if (searchQuery.isNotBlank()){
            launch {
                searchResult = viewModel.getSearchResult(URLEncoder.encode(searchQuery))
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchQuery,
                    onQueryChange = {
                        searchQuery = it
                    },
                    onSearch = {
                        keyboardController!!.hide()
                    },
                    expanded = expandedSearchBar,
                    onExpandedChange = {
                        expandedSearchBar = !expandedSearchBar
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Search")
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                searchQuery = ""
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Clear, null)
                        }
                    }
                )
            },
            expanded = expandedSearchBar,
            onExpandedChange = {
                expandedSearchBar = !expandedSearchBar
            },
            shape = RoundedCornerShape(5.dp),
            colors = SearchBarDefaults.colors(
                containerColor = Color.Transparent,
                dividerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            if (searchResult != null && searchResult?.status == "ok"){

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(vertical = 16.dp, horizontal = 13.dp)
                ){
                    items(items = searchResult?.books.orEmpty()){
                        SearchResultBookItem(book = it){ id ->
                            navController.navigate(Screens.BookScreen.name+"/$id")
                        }
                    }
                }
            }
        }
    }
}