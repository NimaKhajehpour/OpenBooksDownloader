package com.nima.openbooksdownloader.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    var searchResult: SearchResult? by remember {
        mutableStateOf(null)
    }

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
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 10.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth(),
                    shape = CircleShape,
                    placeholder = {
                        Text(text = "Search",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        containerColor = Color.Transparent
                    )
                )
            }
        }

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