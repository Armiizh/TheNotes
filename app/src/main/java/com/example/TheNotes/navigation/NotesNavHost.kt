package com.example.TheNotes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.TheNotes.MainViewModel
import com.example.TheNotes.screens.AddScreen
import com.example.TheNotes.screens.LoginScreen
import com.example.TheNotes.screens.MainScreen
import com.example.TheNotes.screens.EditScreen
import com.example.TheNotes.utils.Constants

sealed class NavRoute(val route: String) {

    data object Main : NavRoute(Constants.Screens.MAIN_SCREEN)
    data object Add : NavRoute(Constants.Screens.ADD_SCREEN)
    data object Edit : NavRoute(Constants.Screens.EDIT_SCREEN)
    data object Login : NavRoute(Constants.Screens.LOGIN_SCREEN)
}

@Composable
fun NotesNavHost(mViewModel: MainViewModel, navController: NavHostController) {

    NavHost(navController = navController, startDestination = NavRoute.Login.route) {
        composable(NavRoute.Main.route) {
            MainScreen(
                navController = navController,
                viewModel = mViewModel
            )
        }
        composable(NavRoute.Add.route) {
            AddScreen(
                navController = navController,
                viewModel = mViewModel
            )
        }
        composable(NavRoute.Edit.route + "/{${Constants.Keys.ID}}") { backStackEntry ->
            EditScreen(
                navController = navController,
                viewModel = mViewModel,
                noteId = backStackEntry.arguments?.getString(Constants.Keys.ID)
            )
        }
        composable(NavRoute.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = mViewModel
            )
        }
    }
}