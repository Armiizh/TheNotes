package com.example.notesappmvvm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notesappmvvm.MainViewModel
import com.example.notesappmvvm.screens.AddScreen
import com.example.notesappmvvm.screens.LoginScreen
import com.example.notesappmvvm.screens.MainScreen
import com.example.notesappmvvm.screens.EditScreen
import com.example.notesappmvvm.utils.Constants

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