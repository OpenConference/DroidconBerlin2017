package de.droidcon.berlin2017.ui.barcamp

import `in`.uncod.android.bypass.Bypass
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2017.clock.Clock
import de.droidcon.berlin2017.interactor.SessionsInteractor
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.schedule.database.SessionAutoValue
import de.droidcon.berlin2017.ui.lce.LceViewState
import de.droidcon.berlin2017.ui.sessions.Sessions
import de.droidcon.berlin2017.ui.sessions.SessionsView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.threeten.bp.ZonedDateTime
import timber.log.Timber
import java.io.IOException

/**
 * Presenter that loads the markdown
 *
 * @author Hannes Dorfmann
 */
class BarCampPresenter(
    private val okHttp: OkHttpClient,
    private val clock: Clock,
    private val bypass: Bypass,
    private val sessionsInteractor: SessionsInteractor) : MviBasePresenter<BarCampView, LceViewState<Sessions>>() {

  private val url = "https://raw.githubusercontent.com/droidcon/gitbook-2017-berlin-barcamp/master/schedule.md"


  override fun bindIntents() {
    val scrolledToNowIntent = intent(SessionsView::scrolledToNowIntent)
        .doOnNext { Timber.d("Scrolled to now intent: $it") }
        .distinctUntilChanged()

    val loadDataIntent = intent(SessionsView::loadDataIntent)
        .doOnNext { Timber.d("Load data intent") }

    val retryLoadDataIntent = intent(SessionsView::retryLoadDataIntent)
        .doOnNext { Timber.d("Retry load data intent") }

    val data = Observable.merge(loadDataIntent, retryLoadDataIntent).switchMap {

      val scheduleObservable = Observable.fromCallable {
        val response = okHttp.newCall(Request.Builder().url(url).get().build()).execute()
        if (!response.isSuccessful) {
          throw IOException("Could not load BarCamp")
        }

        val markdown = response.body()!!.string()
        markdownToSessions(markdown)
      }.subscribeOn(Schedulers.io())

      sessionsInteractor.transform(scrolledToNowIntent) {
        scheduleObservable
      }
    }.observeOn(AndroidSchedulers.mainThread())

    subscribeViewState(data, BarCampView::render)
  }


  /**
   * Parses the markdown file to get the list of sessions
   */
  private fun markdownToSessions(markdown: String): List<Session> {
    val lines = markdown.split("\n")
    val sessionLines = lines.filter {
      val timed = it.trim()
      timed.isNotEmpty() && timed[0].isDigit()
    }
    var id = 0

    return sessionLines.flatMap { line ->

      val slots = line.split("|")
      if (slots.isEmpty())
        emptyList<Session>()
      else {
        val hourMinuteStr = slots[0].trim().split(":")
        val hour = hourMinuteStr[0].trim().toInt()
        val minute = hourMinuteStr[1].trim().toInt()
        val startTime = ZonedDateTime.of(2017, 9, 3, hour, minute, 0, 0,
            clock.getZoneConferenceTakesPlace())
        val endTime = startTime.plusMinutes(20)

        val sessionsInLine = ArrayList<Session>()

        slots.subList(1, slots.size).forEachIndexed { i, title ->
          val trimmed = title.trim()
          if (trimmed.isNotEmpty() && trimmed != "BREAK" && trimmed != "-") {
            sessionsInLine.add(
                SessionAutoValue.create(
                    id = "Barcamp" + id++,
                    speakers = emptyList(),
                    favorite = false,
                    title = bypass.markdownToSpannable(title).toString(),
                    description = "",
                    locationName = "Stage ${(i + 1)}",
                    locationId = "Stage ${(i + 1)}",
                    start = startTime.toInstant(),
                    end = endTime.toInstant(),
                    tags = null
                )
            )
          }
        }

        sessionsInLine
      }
    }
  }
}