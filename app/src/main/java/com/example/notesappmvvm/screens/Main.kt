package com.example.notesappmvvm.screens

import android.app.Application
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.notesappmvvm.MainViewModel
import com.example.notesappmvvm.MainViewModelFactory
import com.example.notesappmvvm.R
import com.example.notesappmvvm.model.Note
import com.example.notesappmvvm.navigation.NavRoute
import com.example.notesappmvvm.utils.Constants
import com.example.notesappmvvm.utils.DB_TYPE
import com.example.notesappmvvm.utils.TYPE_FIREBASE
import com.example.notesappmvvm.utils.TYPE_ROOM
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, viewModel: MainViewModel) {

    val notes = viewModel.readAllNotes().observeAsState(listOf()).value
    val context = LocalContext.current
    val mViewModel: MainViewModel =
        viewModel(factory = MainViewModelFactory(context.applicationContext as Application))

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
                                contentDescription = "",
                                modifier = Modifier.clickable {
                                    mViewModel.signOut {
                                        navController.navigate(NavRoute.Login.route) {
                                            popUpTo(NavRoute.Login.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
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
                LazyColumn(
                    modifier = Modifier
                        .padding(
                            vertical = 6.dp,
                            horizontal = 12.dp
                        )
                ) {
                    items(notes) { note ->
                        NoteItem(note = note, navController = navController, viewModel)
                    }
                }
            }
        }
    )
}

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
    val pin = swipeToPin()

    SwipeableActionsBox(
        modifier = Modifier.clipSwipeActionShape(RoundedCornerShape(16.dp)),
        startActions = listOf(pin),
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
                    Log.d("checkData", "From Main \n Title:${note.title} \nSubtitle:${note.subtitle}")
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

@Composable
private fun swipeToPin(): SwipeAction {
    val pin = SwipeAction(
        onSwipe = {
            Log.d("checkData", "Заметка закреплена")
        },
        icon = {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(
                    id = R.drawable.ic_pin
                ),
                contentDescription = "Pin the note"
            )
        },
        background = colorResource(id = R.color.milkOrange)
    )
    return pin
}

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

fun Modifier.clipSwipeActionShape(shape: Shape): Modifier {
    return this.then(
        Modifier.clip(shape)
    )
}
