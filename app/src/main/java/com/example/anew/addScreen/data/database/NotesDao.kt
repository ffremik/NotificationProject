package com.example.anew.addScreen.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNotes(notesEntity: NotesEntity)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NotesEntity>>

    @Delete
    suspend fun deleteNotes(notes: NotesEntity)

    @Query("SELECT * FROM Notes WHERE requestCode = :requestCode")
    suspend fun getNotes(requestCode: Int): NotesEntity?

}