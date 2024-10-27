package com.example.anew.addScreen.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes")
data class NotesEntity(
    @PrimaryKey()
    val id: String,
    val title: String,
    val textNotes: String,
    val requestCode: Int? = null,
    val informationClock: String? = null
)