package com.example.TheNotes.database.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.TheNotes.database.DatabaseRepository
import com.example.TheNotes.model.Note
import com.example.TheNotes.utils.Constants
import com.example.TheNotes.utils.FIREBASE_ID
import com.example.TheNotes.utils.LOGIN
import com.example.TheNotes.utils.PASSSWORD
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AppFirebaseRepository : DatabaseRepository {

    private val mAuth = FirebaseAuth.getInstance()
    private val database =
        Firebase.database.reference.child("users").child(mAuth.currentUser?.uid.toString())
            .child("notes")
    override val readAll: LiveData<List<Note>> = AllNotesLivaData()

    override suspend fun create(note: Note, onSuccess: () -> Unit) {

        val noteId = database.push().key.toString()
        val mapNotes = hashMapOf<String, Any>()
        mapNotes[FIREBASE_ID] = noteId
        mapNotes[Constants.Keys.TITLE] = note.title
        mapNotes[Constants.Keys.SUBTITLE] = note.subtitle
        mapNotes[Constants.Keys.UPDATEDAT] = note.updatedAt
        database.child(noteId)
            .updateChildren(mapNotes)
            .addOnSuccessListener { onSuccess() }
    }

    override suspend fun update(note: Note, onSuccess: () -> Unit) {
        val noteId = note.firebaseId
        val mapNotes = hashMapOf<String, Any>()
        mapNotes[FIREBASE_ID] = noteId
        mapNotes[Constants.Keys.TITLE] = note.title
        mapNotes[Constants.Keys.SUBTITLE] = note.subtitle
        mapNotes[Constants.Keys.UPDATEDAT] = note.updatedAt
        database.child(noteId)
            .updateChildren(mapNotes)
            .addOnSuccessListener { onSuccess() }
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