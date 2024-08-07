package com.example.TheNotes.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.TheNotes.MainViewModel
import com.example.TheNotes.R
import com.example.TheNotes.model.Note
import com.example.TheNotes.navigation.NavRoute
import com.example.TheNotes.utils.Constants
import com.example.TheNotes.utils.DB_TYPE
import com.example.TheNotes.utils.TYPE_FIREBASE
import com.example.TheNotes.utils.TYPE_ROOM
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel) {

    val notes = viewModel.readAllNotes().observeAsState(listOf()).value
    val openDialog = remember { mutableStateOf(false) }
    val sortedNotes = notes.sortedByDescending { it.updatedAt }
    val searchQuery = remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 2.dp,
                                end = 12.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Notes", style = MaterialTheme.typography.headlineLarge)
                        if (DB_TYPE.value.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Exit",
                                modifier = Modifier.clickable {
                                    openDialog.value = true
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavRoute.Add.route)
                }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = Constants.Keys.ADDTHENOTE
                )
            }
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier.padding(paddingValues)
            ) {
                Column {

                    SearchBar(searchQuery)


                    // Фильтр список заметок по поисковому запросу
                    val filteredNotes = sortedNotes.filter { note ->
                        note.title.contains(searchQuery.value, ignoreCase = true) ||
                                note.subtitle.contains(searchQuery.value, ignoreCase = true)
                    }
                    // Список заметок
                    LazyColumn(
                        modifier = Modifier
                            .padding(
                                vertical = 6.dp,
                                horizontal = 12.dp
                            )
                    ) {
                        items(filteredNotes) { note ->
                            NoteItem(note = note, navController = navController, viewModel)
                        }
                    }
                }
            }
        }
    )

    // Уведомление с подтверждение о выходе
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(
                    text = "Exit account",
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to exit your account?"
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.signOut {
                            navController.navigate(NavRoute.Login.route) {
                                popUpTo(NavRoute.Login.route) {
                                    inclusive = true
                                }
                            }
                        }
                        openDialog.value = false
                    }
                ) {
                    Text(text = "Exit")
                }
            },
            dismissButton = {
                Button(
                    onClick = { openDialog.value = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SearchBar(searchQuery: MutableState<String>) {
    val uiColor =
        if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.6f)
    Card(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 8.dp)
            .padding(horizontal = 12.dp),

        ) {
        Row(
            modifier = Modifier
                .height(24.dp)
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 8.dp, top = 4.dp, bottom = 4.dp),
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search icon",
                tint = Color.Gray
            )
            BasicTextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                textStyle = TextStyle(
                    fontSize = 12.sp,
                    color = uiColor,
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        innerTextField()
                        if (searchQuery.value.isEmpty()) {
                            Text(
                                text = "Search...",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                            )
                        }
                    }
                },
                cursorBrush = SolidColor(Color.Gray)
            )
        }
    }
}

//Заметка
@Composable
fun NoteItem(
    note: Note,
    navController: NavHostController,
    viewModel: MainViewModel,
) {
    val noteId = when (DB_TYPE.value) {
        TYPE_FIREBASE -> note.firebaseId
        TYPE_ROOM -> note.id
        else -> Constants.Keys.EMPTY
    }

    val remove = swipeToRemove(viewModel = viewModel, note)

    SwipeableActionsBox(
        modifier = Modifier
            //.clipSwipeActionShape(RoundedCornerShape(16.dp)),
                ,
        endActions = listOf(remove),
        swipeThreshold = 100.dp,
        backgroundUntilSwipeThreshold = Color.Transparent
    ) {
        Card(
            modifier = Modifier
                .padding(PaddingValues(vertical = 4.dp))
                .fillMaxWidth()
                .clickable {
                    navController.navigate(NavRoute.Edit.route + "/${noteId}")
                    Log.d(
                        "checkData",
                        "From Main \n Title:${note.title} \nSubtitle:${note.subtitle}"
                    )
                },
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                //Заголовок заметки
                Text(
                    style = MaterialTheme.typography.labelLarge,
                    text = note.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row {
                    //Дата и время заметки
                    Text(
                        style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF7789A5)),
                        text = note.updatedAt
                    )
                    //Подзаголовок заметки
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF7789A5)),
                        text = note.subtitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// свайп для удаления заметки
@Composable
private fun swipeToRemove(viewModel: MainViewModel, note: Note): SwipeAction {
    val remove = SwipeAction(
        onSwipe = {
            viewModel.deleteNote(note = note, onSuccess = {})
        },
        icon = {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(
                    id = R.drawable.ic_delete
                ),
                contentDescription = "Delete the note"
            )
        },
        background = colorResource(id = R.color.milkRed)

    )
    return remove
}
