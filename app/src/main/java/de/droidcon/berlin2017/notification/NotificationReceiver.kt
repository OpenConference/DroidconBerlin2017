package de.droidcon.berlin2017.notification

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.content.WakefulBroadcastReceiver
import android.support.v7.app.NotificationCompat
import de.droidcon.berlin2017.DroidconApplication
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.MainActivity
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import timber.log.Timber



/**
 *
 *
 * @author Hannes Dorfmann
 */
class NotificationReceiver : WakefulBroadcastReceiver() {

  companion object {
    private val SESSION_ID = "NotificationBroadcastReceiver.SESSION_ID"
    private var lastSoundVibrate: Long = 0
    private val TIMEOUT_FOR_NOTIFICATION_SOUND_VIBRATE = 1000
    val ACTION = "de.droidcon.berlin2017.notification.NotificationForMySchedule"

    fun showNotificationIntent(context: Context, sessionId: String): Intent {
      val intent = Intent(context, NotificationReceiver::class.java)
      intent.action = ACTION
      intent.putExtra(SESSION_ID, sessionId)
      return intent
    }
  }


  override fun onReceive(context: Context, intent: Intent) {
    Timber.d("Show notification requested")

    try {
      if (intent.action == ACTION) {
        val sessionId = intent.getStringExtra(SESSION_ID)
        if (sessionId == null) {
          Timber.e(
              IllegalStateException("Received an intent to show notification without session id"))
        } else {
          Timber.d("Time to show a notification for session $sessionId")
          showNotification(context, sessionId)
        }
      }
    } catch (t: Throwable) {
      Timber.e(t)
    } finally {
      WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }

  }

  private fun showNotification(context: Context, sessionId: String) {
    val component = DroidconApplication.getApplicationComponent(context)
    val sessionDao = component.sessionRepository()
    val session = sessionDao.getSession(sessionId).blockingFirst()
    Timber.d("Building notification for ${session.title()} and is favorite ${session.favorite()}")

    if (session != null && session.favorite()) {
      val startIntent = MainActivity.buildSessionDetailsIntent(context, session)

      val pendingIntent = PendingIntent.getActivity(context, 0, startIntent, 0)

      val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
      val startStr = timeFormatter.format(
          LocalDateTime.ofInstant(session.startTime(), ZoneId.systemDefault()))


      val notificationManager = context.getSystemService(
          IntentService.NOTIFICATION_SERVICE) as NotificationManager
      val channelId = "MyScheduleSessions"
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val channel = NotificationChannel(channelId,
            "Favorite Sessions",
            NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
      }

      val notificationBuilder = NotificationCompat.Builder(context)
          .setChannelId(channelId)
          .setSmallIcon(R.drawable.ic_notification_small)
          .setContentTitle(context.getString(R.string.notification_title))
          .setContentText(String.format(context.getString(R.string.notification_text),
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


      notificationManager.notify(session.id().hashCode(), notification)
      component.analytics().trackSessionNotificationGenerated(sessionId)
    }
  }

}
