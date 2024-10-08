package com.example.TheNotes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.TheNotes.utils.Constants.Keys.NOTES_APP_TABLE


@Entity(tableName = NOTES_APP_TABLE)
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo
    val title: String = "",
    @ColumnInfo
    val subtitle: String = "",
    val firebaseId: String = "",
    @ColumnInfo
    var updatedAt: String = ""
)
