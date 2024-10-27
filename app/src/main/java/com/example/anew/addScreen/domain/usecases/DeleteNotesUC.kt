package com.example.anew.addScreen.domain.usecases

import com.example.anew.addScreen.domain.Notes
import com.example.anew.addScreen.domain.NotesRepository

class DeleteNotesUC(
    private val notesRepository: NotesRepository
) {
    suspend fun invoke(notes: Notes){
        notesRepository.deleteNotes(notes)
    }
}