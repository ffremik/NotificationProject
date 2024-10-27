package com.example.anew.addScreen.viewmodel


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.anew.MainActivity
import com.example.anew.R
import com.example.anew.addScreen.data.broadcastreceiver.NotificationBroadcastReceiver
import com.example.anew.addScreen.domain.Notes
import com.example.anew.addScreen.domain.usecases.AddNotesUC
import com.example.anew.addScreen.domain.usecases.NotesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddNotesViewModel @Inject constructor(
    private val notesUseCases: NotesUseCases,
    private val context: Application
) : ViewModel() {

    private val _textTitle = MutableStateFlow("")
    val textTitle = _textTitle.asStateFlow()

    private val _textNotes = MutableStateFlow("")
    val textNotes = _textNotes.asStateFlow()

    private val _isAddNotification = MutableStateFlow(false)
    val isAddNotification = _isAddNotification.asStateFlow()
    private val _isOpenDeleteDialog = MutableStateFlow(false)
    val isOpenDeleteDialog = _isOpenDeleteDialog.asStateFlow()

    val hours = MutableStateFlow(0)
    val minutes = MutableStateFlow(0)
    val requestCodeClock: MutableStateFlow<Int?> = MutableStateFlow(null)
    var informationClockNotes: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _repeatNotification = MutableStateFlow(false)
    val repeatNotification = _repeatNotification.asStateFlow()

    val version = MutableStateFlow(0)

    fun updateTime(h: Int, m: Int) {
        hours.value = h
        minutes.value = m
    }

    fun updateIsOpenDeleteDialog() {
        _isOpenDeleteDialog.value = !_isOpenDeleteDialog.value
    }

    @SuppressLint("ScheduleExactAlarm")
    fun notificationNotes() {
        val notificationAlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val requestCode = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
        requestCodeClock.value = requestCode

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hours.value)
            set(Calendar.MINUTE, minutes.value)
            set(Calendar.SECOND, 0)
        }
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(
                Calendar.DAY_OF_YEAR,
                1
            )  // Если время уже прошло, ставим на следующий день
        }
        informationClockNotes.value =
            "Напомнить ${calendar.get(Calendar.DAY_OF_MONTH)} ${getMonthText(calendar.get(Calendar.MONTH))} ${
                getHoursAndMinutesText(calendar.get(Calendar.HOUR_OF_DAY))
            }:${getHoursAndMinutesText(calendar.get(Calendar.MINUTE))}"

        val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra("notificationTitle", textTitle.value)
            putExtra("isRepeatNotification", repeatNotification.value)
            putExtra("requestCode", requestCode)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        notificationAlarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )

    }


    fun updateTextTitle(title: String) {
        _textTitle.value = title
    }

    fun updateTextNotes(text: String) {
        _textNotes.value = text
    }

    fun updateIsRepeatNotification() {
        _repeatNotification.value = !_repeatNotification.value
    }

    fun addNotes() {
        if (isAddNotification.value) {
            notificationNotes()
        }
        if (notesEditing.value != null) {
            viewModelScope.launch {
                _notesEditing.value?.let {
                    notesUseCases.addNotesUC.invoke(
                        it.copy(
                            textNotes = textNotes.value,
                            title = textTitle.value,
                            requestCode = requestCodeClock.value,
                            informationClock = informationClockNotes.value
                        )
                    )
                }
            }
            toastNotification("Изменения сохранены")
        } else {
            viewModelScope.launch {
                notesUseCases.addNotesUC.invoke(
                    Notes(
                        getDateAddNotes(),
                        title = textTitle.value,
                        textNotes = textNotes.value,
                        requestCode = requestCodeClock.value,
                        informationClock = informationClockNotes.value
                    )
                )
            }
            toastNotification("Заметка создана")
        }

    }

    private val _notesEditing: MutableStateFlow<Notes?> = MutableStateFlow(null)
    val notesEditing = _notesEditing.asStateFlow()

    fun updateNotes(notes: Notes) {
        _notesEditing.value = notes
        _textNotes.value = notes.textNotes
        _textTitle.value = notes.title
        informationClockNotes.value = notes.informationClock
    }

    fun getDateAddNotes(): String {
        val date = LocalDate.now().dayOfMonth
        val month = LocalDate.now().month.value
        val time = LocalDate.now().atTime(LocalTime.now())
        return "$date ${getMonthText(month)} в ${time.hour}:${time.minute}"
    }

    fun deleteNotes(notes: Notes) {
        val notificationAlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra("notificationTitle", textTitle.value)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notes.requestCode ?: 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        notificationAlarmManager.cancel(pendingIntent)

        viewModelScope.launch {
            notesUseCases.deleteNotesUC.invoke(notes)
        }
        toastNotification("Заметка была удалена")
    }

    fun deleteNotificationNotes() {
        val notificationAlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
            putExtra("notificationTitle", textTitle.value)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notesEditing.value?.requestCode ?: 0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        notificationAlarmManager.cancel(pendingIntent)

        viewModelScope.launch {
            notesEditing.value?.copy(
                requestCode = null,
                informationClock = null
            )?.let { notesUseCases.addNotesUC.invoke(it) }
        }
        requestCodeClock.value = null
        informationClockNotes.value = null
        updateIsAddNotification()
        toastNotification("Напоминание удалено")
    }

    fun getMonthText(month: Int): String {
        return when (month) {
            0 -> "января"
            1 -> "февраля"
            2 -> "марта"
            3 -> "апрель"
            4 -> "мая"
            5 -> "июня"
            6 -> "июля"
            7 -> "августа"
            8 -> "сентября"
            9 -> "октября"
            10 -> "ноября"
            else -> "декабря"
        }
    }

    fun getHoursAndMinutesText(time: Int): String {
        if (time.toString().length < 2) {
            return "0$time"
        } else {
            return "$time"
        }
    }

    fun toastNotification(textToast: String) {
        Toast.makeText(context, textToast, Toast.LENGTH_LONG).show()
    }

    fun reset() {
        _notesEditing.value = null
        _textNotes.value = ""
        _textTitle.value = ""
        informationClockNotes.value = null
        _isAddNotification.value = false
        _repeatNotification.value = false
    }

    fun updateIsAddNotification() {
        _isAddNotification.value = !_isAddNotification.value
    }
}