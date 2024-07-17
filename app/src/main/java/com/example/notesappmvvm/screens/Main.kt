package com.example.notesappmvvm.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.notesappmvvm.navigation.NavRoute
import com.example.notesappmvvm.ui.theme.NotesAppMVVMTheme

@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavRoute.Add.route)
                }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add The Note"
                )
            }
        },
        content = { PaddingValues ->
            Column(
                modifier = Modifier
                    .padding(PaddingValues(vertical = 8.dp, horizontal = 12.dp))
            ) {
                NoteItem(
                    title = "Note 1",
                    subtitle = "Subtitle for Note 1",
                    navController = navController
                )
                NoteItem(
                    title = "Note 2",
                    subtitle = "Subtitle for Note 2",
                    navController = navController
                )
                NoteItem(
                    title = "Note 3",
                    subtitle = "Subtitle for Note 3",
                    navController = navController
                )
                NoteItem(
                    title = "Note 4",
                    subtitle = "Subtitle for Note 4",
                    navController = navController
                )
            }
        }
    )
}

@Composable
fun NoteItem(
    title: String,
    subtitle: String,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .padding(PaddingValues(vertical = 12.dp, horizontal = 12.dp))
            .fillMaxWidth()
            .clickable {
                navController.navigate(NavRoute.Note.route)
            },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Text(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, end = 8.dp),
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp, end = 8.dp),
            text = subtitle
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrevMainScreen() {
    NotesAppMVVMTheme {
        MainScreen(navController = rememberNavController())
    }
}