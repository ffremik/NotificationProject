package com.example.anew.addScreen.data.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [NotesEntity::class], exportSchema = false, version = 1)
abstract class NotesDatabase() : RoomDatabase(){
    abstract fun getDao() : NotesDao
}