package de.droidcon.berlin2017.notification

import android.app.IntentService
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import android.support.v7.app.NotificationCompat
import de.droidcon.berlin2017.DroidconApplication
import de.droidcon.berlin2017.ui.MainActivity
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import timber.log.Timber
import de.droidcon.berlin2017.R

/**
 *
 *
 * @author Hannes Dorfmann
 */
class NotificationBuilderIntentService : IntentService("NotificationBuilderIntentService") {

  companion object {
    val KEY_SESSION_ID = "NotificationBuilderIntentService.SESSION_ID"
    private var lastSoundVibrate: Long = 0
  }

  val TIMEOUT_FOR_NOTIFICATION_SOUND_VIBRATE = 1000

  override fun onHandleIntent(intent: Intent) {

    try {
      val component = DroidconApplication.getApplicationComponent(applicationContext)
      val sessionDao = component.sessionRepository()
      val sessionId = intent.getStringExtra(
          KEY_SESSION_ID)
      Timber.d("onhandleIntent() $sessionId")

      val session = sessionDao.getSession(sessionId).blockingFirst()

      if (session != null
          && session.favorite()
          && session.startTime() != null
          && Instant.now().isBefore(session.startTime())
          && Instant.now().plusMillis(
          DefaultNotificationScheduler.NOTIFICTAION_BEFORE_SESSION_START).isAfter(
          session.startTime())
          ) {
        // Build Notification
        if (session.title() == null) {
          Timber.e(RuntimeException("Title of Session ${session.id()} is null: $session"),
              "Title of Session is null")
        } else {

          val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
          val startStr = timeFormatter.format(
              LocalDateTime.ofInstant(session.startTime(), ZoneId.systemDefault()))

          val startIntent = MainActivity.buildSessionDetailsIntent(applicationContext, session)

          val pendingIntent = PendingIntent.getActivity(applicationContext, 0, startIntent, 0)

          val notificationBuilder = NotificationCompat.Builder(applicationContext)
              .setSmallIcon(R.drawable.ic_notification_small)
              .setContentTitle(application.getString(R.string.notification_title))
              .setContentText(String.format(application.getString(R.string.notification_text),
                  session.title(), startStr))
              .setContentIntent(pendingIntent)
              .setAutoCancel(true)

          if (System.currentTimeMillis() - lastSoundVibrate > TIMEOUT_FOR_NOTIFICATION_SOUND_VIBRATE) {
            notificationBuilder.setDefaults(
                // Vibrate and play sound, but not all the time
                Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
            lastSoundVibrate = System.currentTimeMillis()
          }


          val notification = notificationBuilder.build()

          val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
          notificationManager.notify(session.id().hashCode(), notification)
        }
      }

    } finally {
      WakefulBroadcastReceiver.completeWakefulIntent(intent)
    }
  }
}