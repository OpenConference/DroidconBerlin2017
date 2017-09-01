package de.droidcon.berlin2017.ui.twitter

import android.content.Context
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.tweetui.TweetUi
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import kotlin.system.measureTimeMillis

/**
 * Since Twitter is causing a lot of ANR
 * this workaround state machine is required
 *
 * @author Hannes Dorfmann
 */
class TwitterInitializer(context: Context) {

  private val internalSubject = BehaviorSubject.createDefault<TwitterState>(
      TwitterState.IN_PROGRESS)
  val state: Observable<TwitterState> = internalSubject

  init {
    Thread({
      // Causes ANR --> workaround, move it into background thread
      try {
       val duration =  measureTimeMillis {
          Twitter.initialize(context)
          TweetUi.getInstance()
        }
        Timber.d("Twitter initilized in $duration ms")
        internalSubject.onNext(TwitterState.READY)
      } catch (t: Throwable) {
        Timber.e(t)
        // We never get ready :( sorry dear user --> Twitter wtf
      }
    }).start()
  }


}