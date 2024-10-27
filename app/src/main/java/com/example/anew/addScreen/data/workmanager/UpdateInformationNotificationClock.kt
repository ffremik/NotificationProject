package com.example.anew.addScreen.data.workmanager

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.anew.addScreen.data.database.NotesDatabase
import com.example.anew.addScreen.domain.usecases.GetNotesUC
import com.example.anew.addScreen.domain.usecases.NotesUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject


class UpdateInformationNotificationClock(
    appContext: Context,
    workerParameters: WorkerParameters,

    ) : CoroutineWorker(appContext, workerParameters) {
    val notesDao = Room.databaseBuilder(
        appContext,
        NotesDatabase::class.java,
        "NotesBase"
    ).build().getDao()

    override suspend fun doWork(): Result {
        val requestCodeNotes = inputData.getInt("requestCode", 0)
        val getNotes = notesDao.getNotes(requestCodeNotes)
        val updateTextNotification = inputData.getString("updateTextNotification")

        return try {
            if (updateTextNotification != null && getNotes != null) {
                notesDao.addNotes(getNotes.copy(informationClock = updateTextNotification))

            } else {
                if (getNotes != null) {
                    notesDao.addNotes(getNotes.copy(informationClock = null, requestCode = null))

                }
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }


    }

}