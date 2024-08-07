package com.example.TheNotes.utils

import androidx.compose.runtime.mutableStateOf
import com.example.TheNotes.database.DatabaseRepository

const val TYPE_ROOM = "type_room"
const val TYPE_FIREBASE = "type_firebase"
const val FIREBASE_ID = "firebaseId"

lateinit var REPOSITORY: DatabaseRepository
lateinit var LOGIN: String
lateinit var PASSSWORD: String
var DB_TYPE = mutableStateOf("")

object Constants {
    object Keys {
        const val TITLE = "title"
        const val LOGIN_TEXT = "Login"
        const val PASSWORD_TEXT = "Password"
        const val SUBTITLE = "subtitle"
        const val UPDATEDAT = "updatedAt"
        const val ADDTHENOTE = "Add The Note"
        const val NOTE_DATABASE = "notes_database"
        const val NOTES_APP_TABLE = "notesApp_table"
        const val NOTES_TABLE = "notes_table"
        const val ID = "id"
        const val LOG_IN = "Log in or Sign up"
        const val LOG_IN_GUEST = "Guest"
        const val SHOW_PASSWORD = "Show password"
        const val HIDE_PASSWORD = "Hide password"
        const val EMPTY = ""
        const val DONT_HAVE_ACC = "If you don't have an account yet, we will create one for you"
        const val CONTINUE_AS_GUEST = "Or continue as"
        const val INVALID_PASSWORD = "Invalid password"
        const val INVALID_EMAIL = "Invalid email"
    }

    object Screens {
        const val MAIN_SCREEN = "main_screen"
        const val ADD_SCREEN = "add_screen"
        const val EDIT_SCREEN = "edit_screen"
        const val LOGIN_SCREEN = "login_screen"
    }
}