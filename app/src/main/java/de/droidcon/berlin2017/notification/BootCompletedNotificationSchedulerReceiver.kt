package de.droidcon.berlin2017.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import de.droidcon.berlin2017.DroidconApplication

/**
 * Called after boot completed to reschedule all the user's favorite sessions,
 * so that he will sees a notification before the session starts
 *
 * @author Hannes Dorfmann
 */
class BootCompletedNotificationSchedulerReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    if (intent.getAction() == ("android.intent.action.BOOT_COMPLETED")) {
      // Set the alarm here.
      val component = DroidconApplication.getApplicationComponent(context)
      val notificationScheduler = component.notificationScheduler()
      val favoriteSessions = component.sessionRepository().favoriteSessions().blockingFirst()
      favoriteSessions.forEach {
        notificationScheduler.addOrRescheduleNotification(it)
      }

    }
  }
}