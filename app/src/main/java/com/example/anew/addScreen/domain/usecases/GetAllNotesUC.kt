package com.example.anew.addScreen.domain.usecases

import com.example.anew.addScreen.domain.Notes
import com.example.anew.addScreen.domain.NotesRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotesUC(
    private val notesRepository: NotesRepository
) {
    fun invoke(): Flow<List<Notes>> {
        return notesRepository.getAllNotes()
    }
}