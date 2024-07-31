@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.notesappmvvm.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.notesappmvvm.MainViewModel
import com.example.notesappmvvm.model.Note
import com.example.notesappmvvm.navigation.NavRoute
import com.example.notesappmvvm.utils.Constants
import com.example.notesappmvvm.utils.DB_TYPE
import com.example.notesappmvvm.utils.TYPE_FIREBASE
import com.example.notesappmvvm.utils.TYPE_ROOM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(navController: NavHostController, viewModel: MainViewModel, noteId: String?) {

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

    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf(Constants.Keys.EMPTY) }
    var subtitle by remember { mutableStateOf(Constants.Keys.EMPTY) }

    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = bottomSheetState,
            shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
            onDismissRequest = {
                showBottomSheet = false
            }
        ) {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(PaddingValues(32.dp))
                ) {
                    Text(
                        text = Constants.Keys.EDIT_NOTE,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(text = Constants.Keys.TITLE) },
                        isError = title.isEmpty()
                    )
                    OutlinedTextField(
                        value = subtitle,
                        onValueChange = { subtitle = it },
                        label = { Text(text = Constants.Keys.SUBTITLE) },
                        isError = subtitle.isEmpty()
                    )
                    //Update  btn in Sheet
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            val updatedAt = SimpleDateFormat("dd.MM.yy HH.mm").format(Date(System.currentTimeMillis()))
                            viewModel.updateNote(
                                note = Note(
                                    id = note.id,
                                    title = title,
                                    subtitle = subtitle,
                                    firebaseId = note.firebaseId,
                                    updatedAt = updatedAt
                                )
                            ) {
                                navController.navigate(NavRoute.Main.route)
                            }
                        }
                    ) {
                        Text(text = Constants.Keys.UPDATE_NOTE)
                    }
                }
            }
        }
    }

    EditSection(
        note,
        showBottomSheet,
        coroutineScope,
        bottomSheetState,
        title,
        subtitle,
        navController
    )
}

@Composable
private fun EditSection(
    note: Note,
    showBottomSheet: Boolean,
    coroutineScope: CoroutineScope,
    bottomSheetState: SheetState,
    title: String,
    subtitle: String,
    navController: NavHostController
) {
    var showBottomSheet1 = showBottomSheet
    var title1 = title
    var subtitle1 = subtitle

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { PaddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PaddingValues(8.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = note.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                top = 12.dp,
                                start = 8.dp,
                                end = 8.dp
                            )
                        )
                        Text(
                            text = note.subtitle,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(paddingValues = PaddingValues(12.dp))
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {


                    //Update btn
                    Button(
                        onClick = {
                            showBottomSheet1 = true
                            coroutineScope.launch {
                                bottomSheetState.show()
                                title1 = note.title
                                subtitle1 = note.subtitle
                            }
                        }
                    ) {
                        Text(text = Constants.Keys.UPDATE)
                    }
                }

                //Back to main btn
                Button(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth(),
                    onClick = {
                        navController.navigate(NavRoute.Main.route)
                    }
                ) {
                    Text(text = Constants.Keys.NAV_BACK)
                }
            }
        }
    )
}