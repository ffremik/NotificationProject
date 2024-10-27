package com.example.anew.addScreen.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.anew.addScreen.data.database.NotesDao
import com.example.anew.addScreen.data.database.NotesDatabase
import com.example.anew.addScreen.data.repository.NotesRepositoryImpl
import com.example.anew.addScreen.data.workmanager.UpdateInformationNotificationClock
import com.example.anew.addScreen.domain.NotesRepository
import com.example.anew.addScreen.domain.usecases.AddNotesUC
import com.example.anew.addScreen.domain.usecases.DeleteNotesUC
import com.example.anew.addScreen.domain.usecases.GetAllNotesUC
import com.example.anew.addScreen.domain.usecases.GetNotesUC
import com.example.anew.addScreen.domain.usecases.NotesUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun provideNotesDao(app: Application): NotesDao {
        return Room.databaseBuilder(
            app,
            NotesDatabase::class.java,
            "NotesBase"
        ).build().getDao()
    }

    @Provides
    @Singleton
    fun provideNotesRepository(notesDao: NotesDao): NotesRepository {
        return NotesRepositoryImpl(notesDao)
    }

    @Provides
    @Singleton
    fun provideNotesUseCases(notesRepository: NotesRepository): NotesUseCases {
        return NotesUseCases(
            AddNotesUC(notesRepository),
            GetAllNotesUC(notesRepository),
            DeleteNotesUC(notesRepository),
            GetNotesUC(notesRepository)
        )
    }

}