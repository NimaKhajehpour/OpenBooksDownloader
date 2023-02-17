package com.nima.openbooksdownloader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nima.openbooksdownloader.screens.HomeScreen

@Composable
fun OpenBooksNavigation (){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.HomeScreen.name){

        composable(Screens.HomeScreen.name){
            HomeScreen(navController = navController, viewModel = hiltViewModel())
        }
    }
}