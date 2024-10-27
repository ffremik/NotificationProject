package com.example.anew.addScreen.domain.usecases

import com.example.anew.addScreen.domain.Notes
import com.example.anew.addScreen.domain.NotesRepository
import kotlinx.coroutines.flow.Flow

class GetNotesUC(
    private val notesRepository: NotesRepository
) {
    suspend fun invoke(requestCode: Int): Notes {
        return notesRepository.getNotes(requestCode)
    }
}