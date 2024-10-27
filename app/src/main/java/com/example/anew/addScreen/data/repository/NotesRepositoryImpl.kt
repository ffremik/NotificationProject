package com.example.anew.addScreen.data.repository

import com.example.anew.addScreen.data.database.NotesDao
import com.example.anew.addScreen.data.mapper.toNotes
import com.example.anew.addScreen.data.mapper.toNotesEntity
import com.example.anew.addScreen.domain.Notes
import com.example.anew.addScreen.domain.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl(private val notesDao: NotesDao) : NotesRepository {
    override suspend fun addNotes(notes: Notes) {
        notesDao.addNotes(
            notes.toNotesEntity()
        )
    }

    override fun getAllNotes(): Flow<List<Notes>> {
        return notesDao.getAllNotes().map {
            it.map { notesEntity ->
                notesEntity.toNotes()
            }
        }
    }

    override suspend fun deleteNotes(notes: Notes) {
        notesDao.deleteNotes(notes.toNotesEntity())
    }

    override suspend fun getNotes(requestCode: Int): Notes {
       return notesDao.getNotes(requestCode)?.toNotes() ?: Notes(
           id = "не найден",
           title = "Нету",
           textNotes = "Нету",
           requestCode = null,
           informationClock = null
       )

    }

}