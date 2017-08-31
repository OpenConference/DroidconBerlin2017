package de.droidcon.berlin2017.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import de.droidcon.berlin2017.model.Session
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Default [NotificationScheduler] for android
 *
 * @author Hannes Dorfmann
 */
class DefaultNotificationScheduler(private val context: Context,
    private val intentToBroadcastReceiver: (Context, Session) -> Intent) : NotificationScheduler {

  companion object {
    // TODO add settings
    private val NOTIFICATION_BEFORE_SESSION_START = TimeUnit.MINUTES.toMillis(10) // 10 Minutes
  }


  private fun alarmService(): AlarmManager =
      context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

  private fun buildPendingIntent(s: Session): PendingIntent {
    return PendingIntent.getBroadcast(context, 0, intentToBroadcastReceiver(context, s), 0)
  }


  override fun removeNotification(session: Session) {
    alarmService().cancel(buildPendingIntent(session))
  }

  override fun addOrRescheduleNotification(session: Session) {

    removeNotification(session)

    val startTime = session.startTime() ?: return


    if (startTime.isBefore(Instant.now())) // not schedule notification because it was in the past
      return


    val intent = buildPendingIntent(session)

    val notificationTime = startTime.minusMillis(NOTIFICATION_BEFORE_SESSION_START)

    val localTime = LocalDateTime.ofInstant(notificationTime, ZoneId.systemDefault())
    Timber.d("Schedule notification at $localTime  " + LocalDateTime.now(
        ZoneId.systemDefault()) + " scheduled at UnixTimestamp: " + notificationTime.toEpochMilli())

    if (Build.VERSION.SDK_INT < 23) {
      alarmService().setExact(AlarmManager.RTC_WAKEUP,
          notificationTime.toEpochMilli(), intent)
    } else {
      alarmService().setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
          notificationTime.toEpochMilli(), intent)
    }
  }
}