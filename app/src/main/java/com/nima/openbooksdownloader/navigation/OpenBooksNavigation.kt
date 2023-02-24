package com.nima.openbooksdownloader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nima.openbooksdownloader.screens.*

@Composable
fun OpenBooksNavigation (){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.HomeScreen.name){

        composable(Screens.HomeScreen.name){
            HomeScreen(navController = navController, viewModel = hiltViewModel())
        }

        composable(Screens.BookScreen.name+"/{id}",
            arguments = listOf(
                navArgument(name = "id"){type = NavType.StringType}
            )
        ){
            BookScreen(navController = navController, viewModel = hiltViewModel(),
                id = it.arguments?.getString("id"))
        }

        composable(Screens.SearchScreen.name){
            SearchScreen(navController = navController, viewModel = hiltViewModel())
        }

        composable(Screens.TagsScreen.name){
            TagsScreen(navController = navController, viewModel = hiltViewModel())
        }

        composable(Screens.BookmarkScreen.name){
            BookmarkScreen(navController = navController, viewModel = hiltViewModel())
        }
    }
}