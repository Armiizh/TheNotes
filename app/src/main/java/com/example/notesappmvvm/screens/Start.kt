package com.example.notesappmvvm.screens

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.notesappmvvm.MainViewModel
import com.example.notesappmvvm.MainViewModelFactory
import com.example.notesappmvvm.navigation.NavRoute
import com.example.notesappmvvm.ui.theme.NotesAppMVVMTheme
import com.example.notesappmvvm.utils.TYPE_FIREBASE
import com.example.notesappmvvm.utils.TYPE_ROOM

@Composable
fun StartScreen(navController: NavHostController) {

    val context = LocalContext.current
    val mViewModel: MainViewModel =
        viewModel(factory = MainViewModelFactory(context.applicationContext as Application))

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "What well we use?")
                Button(
                    onClick = {
                        mViewModel.initDatabase(TYPE_ROOM)
                        navController.navigate(route = NavRoute.Main.route)
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = "Room database")
                }
                Button(
                    onClick = {
                        mViewModel.initDatabase(TYPE_FIREBASE)
                        navController.navigate(route = NavRoute.Main.route)
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .padding(vertical = 8.dp)
                ) {
                    Text(text = "Firebase database")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PrevStartScreen() {
    NotesAppMVVMTheme {
        StartScreen(navController = rememberNavController())
    }
}