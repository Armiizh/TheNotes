package com.example.TheNotes.database.room.repository

import androidx.lifecycle.LiveData
import com.example.TheNotes.database.DatabaseRepository
import com.example.TheNotes.database.room.dao.NoteRoomDao
import com.example.TheNotes.model.Note

class RoomRepository(private val noteRoomDao: NoteRoomDao) : DatabaseRepository {

    override val readAll: LiveData<List<Note>>
        get() = noteRoomDao.getAllNotes()

    override fun signOut() {}

    override suspend fun create(note: Note, onSuccess: () -> Unit) {
        noteRoomDao.addNote(note = note)
        onSuccess()
    }

    override suspend fun update(note: Note, onSuccess: () -> Unit) {
        noteRoomDao.updateNote(note = note)
        onSuccess()
    }

    override suspend fun delete(note: Note, onSuccess: () -> Unit) {
        noteRoomDao.deleteNote(note = note)
        onSuccess()
    }
}