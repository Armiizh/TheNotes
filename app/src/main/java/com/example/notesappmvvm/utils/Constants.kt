package com.example.notesappmvvm.utils

import androidx.compose.runtime.mutableStateOf
import com.example.notesappmvvm.database.DatabaseRepository

const val TYPE_ROOM = "type_room"
const val TYPE_FIREBASE = "type_firebase"
const val FIREBASE_ID = "firebaseId"

lateinit var REPOSITORY: DatabaseRepository
lateinit var LOGIN: String
lateinit var PASSSWORD: String
var DB_TYPE = mutableStateOf("")

object Constants {
    object Keys {
        const val ADD_NEW_NOTE = "Add new note"
        const val NOTE_TITLE = "Note title"
        const val NOTE_SUBTITLE = "Note subtitle"
        const val ADD_NOTE = "Add note"
        const val TITLE = "title"
        const val LOGIN_TEXT = "Login"
        const val PASSWORD_TEXT = "Password"
        const val SUBTITLE = "subtitle"
        const val NOTE_DATABASE = "notes_database"
        const val NOTES_APP_TABLE = "notesApp_table"
        const val NOTES_TABLE = "notes_table"
        const val WHAT_WILL_WE_USE = "What will we use?"
        const val ROOM_DATABASE = "Room database"
        const val FIREBASE_DATABASE = "Firebase database"
        const val ID = "id"
        const val UPDATE = "Update"
        const val DELETE = "Delete"
        const val NAV_BACK = "Nav_back"
        const val EDIT_NOTE = "Edit note"
        const val LOG_IN = "Log in"
        const val EMPTY = ""
        const val UPDATE_NOTE = "Update note"
        const val SIGN_IN = "Sign in"
    }

    object Screens {
        const val START_SCREEN = "start_screen"
        const val MAIN_SCREEN = "main_screen"
        const val ADD_SCREEN = "add_screen"
        const val NOTE_SCREEN = "note_screen"
        const val LOGIN_SCREEN = "login_screen"
    }
}