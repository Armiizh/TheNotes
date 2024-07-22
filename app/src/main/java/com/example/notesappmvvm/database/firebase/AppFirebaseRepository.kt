package com.example.notesappmvvm.database.firebase

import androidx.lifecycle.LiveData
import com.example.notesappmvvm.database.DatabaseRepository
import com.example.notesappmvvm.model.Note
import com.example.notesappmvvm.utils.LOGIN
import com.example.notesappmvvm.utils.PASSSWORD
import com.google.firebase.auth.FirebaseAuth

class AppFirebaseRepository : DatabaseRepository {

    private val mAuth = FirebaseAuth.getInstance()

    override val readAll: LiveData<List<Note>>
        get() = TODO()

    override suspend fun create(note: Note, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun update(note: Note, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(note: Note, onSuccess: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun signOut() {
        mAuth.signOut()
    }

    override fun connectToDatabase(onSuccess: () -> Unit, onFail: (Throwable) -> Unit) {
        mAuth.signInWithEmailAndPassword(LOGIN, PASSSWORD)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { mAuth.createUserWithEmailAndPassword(LOGIN, PASSSWORD)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { expection ->
                    onFail(expection)
                }}

    }
}