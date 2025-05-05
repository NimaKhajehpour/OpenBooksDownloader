package com.nima.openbooksdownloader.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nima.openbooksdownloader.screens.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun OpenBooksNavigation (navController: NavHostController){

    NavHost(navController = navController, startDestination = Screens.HomeScreen.name){

        composable(Screens.HomeScreen.name){
            HomeScreen(navController = navController, viewModel = koinViewModel())
        }

        composable(Screens.BookScreen.name+"/{id}",
            arguments = listOf(
                navArgument(name = "id"){type = NavType.StringType}
            )
        ){
            BookScreen(navController = navController, viewModel = koinViewModel(),
                id = it.arguments?.getString("id"))
        }

        composable(Screens.SearchScreen.name){
            SearchScreen(navController = navController, viewModel = koinViewModel())
        }

        composable(Screens.TagsScreen.name){
            TagsScreen(navController = navController, viewModel = koinViewModel())
        }

        composable(Screens.BookmarkScreen.name){
            BookmarkScreen(navController = navController, viewModel = koinViewModel())
        }

        composable(Screens.SavedBookScreen.name+"/{id}/{name}",
            arguments = listOf(
                navArgument(name = "id"){type = NavType.StringType},
                navArgument("name"){NavType.StringType}
            )
        ){
            SavedBookScreen(navController = navController, viewModel = koinViewModel(),
                id = it.arguments?.getString("id"), name = it.arguments?.getString("name"))
        }

        composable(Screens.DownloadsScreen.name){
            DownloadsScreen(navController = navController, viewModel = koinViewModel())
        }

        composable(Screens.TagScreen.name+"/{tag}",
            arguments = listOf(
                navArgument(name = "tag"){type = NavType.StringType}
            )
        ){
            TagScreen(navController = navController, viewModel = koinViewModel(),
                tag = it.arguments?.getString("tag"))
        }

        composable(Screens.DonateScreen.name){
            DonateScreen(navController = navController)
        }

        composable(Screens.AboutScreen.name){
            AboutScreen(navController = navController)
        }
    }
}