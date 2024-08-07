package com.example.TheNotes.database


import androidx.lifecycle.LiveData
import com.example.TheNotes.model.Note

interface DatabaseRepository {

    val readAll: LiveData<List<Note>>

    suspend fun create(note: Note, onSuccess: () -> Unit)

    suspend fun update(note: Note, onSuccess: () -> Unit)

    suspend fun delete(note: Note, onSuccess: () -> Unit)

    fun signOut() {}

    fun connectToDatabase(onSuccess: () -> Unit, onFail: (Throwable) -> Unit) {}
}