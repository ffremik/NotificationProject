package com.example.anew.addScreen.data.broadcastreceiver

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.CalendarContract.Calendars
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.anew.R
import com.example.anew.addScreen.data.workmanager.UpdateInformationNotificationClock
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class NotificationBroadcastReceiver() : BroadcastReceiver() {
    @SuppressLint("UnsafeIntentLaunch", "ScheduleExactAlarm")
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val textTitleNotification = intent?.getStringExtra("notificationTitle")
        val isRepeatNotification = intent?.getBooleanExtra("isRepeatNotification", false) ?: false
        val requestCode = intent?.getIntExtra("requestCode", 0) ?: 0

        val resultScreen = Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        val resultMode = Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0)
        val resultName = Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME, )
        val result = Settings.Global.getInt(context.contentResolver, Settings.Global.WIFI_ON, 0)

        Log.i("MyLog", "requestCode ${requestCode}")
        Log.i("MyLog", "Яркость экрана ${resultScreen}")
        Log.i("MyLog", "включен режим полета? ${resultMode}")
        Log.i("MyLog", "Имя модели? ${resultName}")
        Log.i("MyLog", "Подключен ли wifi? ${result}")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "nots",
                "notificationNotes",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(context, "nots")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Уведомление")
                .setContentText("Вы просили напомнить о: $textTitleNotification")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .build()

            notificationManager.notify(1001, notification)


            val timeCalendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, 1)
            }

            val requestWorker =
                if (isRepeatNotification) OneTimeWorkRequestBuilder<UpdateInformationNotificationClock>()
                    .setInputData(
                        workDataOf(
                            "requestCode" to requestCode, "updateTextNotification" to "Напомнить ${
                                timeCalendar.get(
                                    Calendar.DAY_OF_MONTH
                                )
                            } ${getMonthText(timeCalendar.get(Calendar.MONTH))} ${
                                getHoursAndMinutesText(timeCalendar.get(Calendar.HOUR_OF_DAY))
                            }:${getHoursAndMinutesText(timeCalendar.get(Calendar.MINUTE))}"
                        )
                    )
                    .build() else
                    OneTimeWorkRequestBuilder<UpdateInformationNotificationClock>()
                        .setInputData(workDataOf("requestCode" to requestCode))
                        .build()
            if (isRepeatNotification) {
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent!!,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeCalendar.timeInMillis,
                    pendingIntent
                )
                Log.i("MyLog", "Был поставлен ещё раз будильник теперь он стоит на ${timeCalendar.get(Calendar.DAY_OF_MONTH)}")


            }
            WorkManager
                .getInstance(context)
                .enqueue(requestWorker)

        } else {

        }
    }

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