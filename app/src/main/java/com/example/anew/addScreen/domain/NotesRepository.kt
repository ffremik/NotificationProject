package com.example.anew.addScreen.domain

import com.example.anew.addScreen.domain.Notes
import kotlinx.coroutines.flow.Flow


interface NotesRepository {
    suspend fun addNotes(notes: Notes)
    fun getAllNotes(): Flow<List<Notes>>
    suspend fun deleteNotes(notes: Notes)
    suspend fun getNotes(requestCode: Int): Notes
}