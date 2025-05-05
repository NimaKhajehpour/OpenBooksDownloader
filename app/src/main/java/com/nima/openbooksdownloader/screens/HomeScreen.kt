package com.nima.openbooksdownloader.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
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


    Column (
        modifier = Modifier
            .fillMaxWidth(),
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