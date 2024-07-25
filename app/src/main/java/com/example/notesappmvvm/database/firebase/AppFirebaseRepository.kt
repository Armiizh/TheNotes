package com.example.notesappmvvm.database.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.notesappmvvm.database.DatabaseRepository
import com.example.notesappmvvm.model.Note
import com.example.notesappmvvm.utils.Constants
import com.example.notesappmvvm.utils.FIREBASE_ID
import com.example.notesappmvvm.utils.LOGIN
import com.example.notesappmvvm.utils.PASSSWORD
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AppFirebaseRepository : DatabaseRepository {

    private val mAuth = FirebaseAuth.getInstance()
    private val database = Firebase.database.reference.child("users").child(mAuth.currentUser?.uid.toString()).child("notes")
    override val readAll: LiveData<List<Note>> = AllNotesLivaData()

    override suspend fun create(note: Note, onSuccess: () -> Unit) {

        val noteId = database.push().key.toString()
        val mapNotes = hashMapOf<String, Any>()
        mapNotes[FIREBASE_ID] = noteId
        mapNotes[Constants.Keys.TITLE] = note.title
        mapNotes[Constants.Keys.SUBTITLE] = note.subtitle
        database.child(noteId)
            .updateChildren(mapNotes)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { Log.d("checkData", "Не удалось залить заметку на Firebase") }
    }

    override suspend fun update(note: Note, onSuccess: () -> Unit) {
        val noteId = note.firebaseId
        val mapNotes = hashMapOf<String, Any>()
        mapNotes[FIREBASE_ID] = noteId
        mapNotes[Constants.Keys.TITLE] = note.title
        mapNotes[Constants.Keys.SUBTITLE] = note.subtitle
        database.child(noteId)
            .updateChildren(mapNotes)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { Log.d("checkData", "Не удалось залить заметку на Firebase") }
    }

    override suspend fun delete(note: Note, onSuccess: () -> Unit) {
        database.child(note.firebaseId).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { Log.d("checkData", "Failed to update note") }
    }

    override fun signOut() {

    }

    override fun connectToDatabase(onSuccess: () -> Unit, onFail: (Throwable) -> Unit) {
        mAuth.signInWithEmailAndPassword(LOGIN, PASSSWORD)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                mAuth.createUserWithEmailAndPassword(LOGIN, PASSSWORD)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { expection ->
                        onFail(expection)
                    }
            }
    }
}