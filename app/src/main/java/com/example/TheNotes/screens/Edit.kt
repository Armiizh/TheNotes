@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.TheNotes.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.TheNotes.MainViewModel
import com.example.TheNotes.R
import com.example.TheNotes.model.Note
import com.example.TheNotes.navigation.NavRoute
import com.example.TheNotes.utils.Constants
import com.example.TheNotes.utils.DB_TYPE
import com.example.TheNotes.utils.TYPE_FIREBASE
import com.example.TheNotes.utils.TYPE_ROOM
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(navController: NavHostController, viewModel: MainViewModel, noteId: String?) {

    val notes = viewModel.readAllNotes().observeAsState(listOf()).value
    val note = when (DB_TYPE.value) {
        TYPE_ROOM -> {
            notes.firstOrNull { it.id == noteId?.toInt() } ?: Note()
        }

        TYPE_FIREBASE -> {
            notes.firstOrNull { it.firebaseId == noteId } ?: Note()
        }

        else -> Note()
    }
    var title by remember { mutableStateOf(TextFieldValue(Constants.Keys.EMPTY)) }
    var subtitle by remember { mutableStateOf(TextFieldValue(Constants.Keys.EMPTY)) }

    val focusRequester = remember { FocusRequester() }

    title = TextFieldValue(note.title)
    subtitle = TextFieldValue(note.subtitle)

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                end = 12.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            Modifier.clickable {
                                navController.navigate(NavRoute.Main.route)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "back"
                            )
                            Text(text = "Notes")
                        }
                        Text(
                            text = "Done",
                            modifier = Modifier.clickable {
                                if (title.text.isNotEmpty() || (title.text.isNotEmpty() && subtitle.text.isNotEmpty())) {
                                    val updatedAt =
                                        SimpleDateFormat("dd.MM.yy HH.mm").format(Date(System.currentTimeMillis()))
                                    viewModel.updateNote(
                                        note = Note(
                                            id = note.id,
                                            title = title.text,
                                            subtitle = subtitle.text,
                                            firebaseId = note.firebaseId,
                                            updatedAt = updatedAt
                                        )
                                    ) {
                                        navController.navigate(NavRoute.Main.route)
                                    }
                                } else {
                                    navController.navigate(NavRoute.Main.route)
                                }
                            })
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(Color.Transparent)
            ) {
                Column {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(0.dp, Color.Transparent))
                            .focusRequester(focusRequester),
                        value = title.copy(selection = TextRange(title.text.length)),
                        onValueChange = {
                            title = it.copy(selection = TextRange(it.text.length))
                        },
                        textStyle = MaterialTheme.typography.headlineMedium,
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Sentences
                        )
                    )
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(0.dp, Color.Transparent)),
                        value = subtitle,
                        onValueChange = {
                            subtitle = it.copy(selection = TextRange(it.text.length))
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Sentences
                        )
                    )
                }
            }
        }
    )
}