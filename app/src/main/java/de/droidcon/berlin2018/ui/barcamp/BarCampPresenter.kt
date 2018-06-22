package de.droidcon.berlin2018.ui.barcamp

import `in`.uncod.android.bypass.Bypass
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter
import de.droidcon.berlin2018.clock.Clock
import de.droidcon.berlin2018.interactor.SessionsInteractor
import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.schedule.database.SessionAutoValue
import de.droidcon.berlin2018.ui.lce.LceViewState
import de.droidcon.berlin2018.ui.sessions.Sessions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.CacheControl
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
    private val sessionsInteractor: SessionsInteractor
) : MviBasePresenter<BarCampView, LceViewState<Sessions>>() {

    private val url =
        "https://raw.githubusercontent.com/droidcon/gitbook-2018-berlin-barcamp/master/schedule.md"


    override fun bindIntents() {
        val scrolledToNowIntent = intent(BarCampView::scrolledToNowIntent)
            .doOnNext { Timber.d("Scrolled to now intent: $it") }
            .distinctUntilChanged()

        val loadDataIntent = intent(BarCampView::loadDataIntent)
            .doOnNext { Timber.d("Load data intent") }

        val pullToRefreshIntent = intent(BarCampView::swipeRefreshIntent)
            .doOnNext { Timber.d("PullToRefresh data intent") }

        val retryLoadDataIntent = intent(BarCampView::retryLoadDataIntent)
            .doOnNext { Timber.d("Retry load data intent") }

        val data =
            Observable.merge(loadDataIntent, retryLoadDataIntent, pullToRefreshIntent).switchMap {

                val scheduleObservable = Observable.fromCallable {
                    val response = okHttp.newCall(
                        Request.Builder().url(url).get()
                            .cacheControl(CacheControl.FORCE_NETWORK).build()
                    ).execute()
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
            timed.isNotEmpty() && timed[0] == '|' && timed.length > 3 && timed[2].isDigit()
        }
        var id = 0

        return sessionLines.flatMap { line ->

            val slots = line.split("|").filter { it.isNotBlank() }
            if (slots.isEmpty())
                emptyList<Session>()
            else {
                val hourMinuteStr = slots[0].trim().split(":")
                val hour = hourMinuteStr[0].trim().toInt()
                val minute = hourMinuteStr[1].trim().toInt()
                val startTime = ZonedDateTime.of(
                    2018, 6, 25, hour, minute, 0, 0,
                    clock.getZoneConferenceTakesPlace()
                )
                val endTime = startTime.plusMinutes(30)

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
                                locationName = getStage(i),
                                locationId = getStage(i),
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

    private fun getStage(index: Int) = when (index) {
        0 -> "Oreo"
        1 -> "Cupcake"
        2 -> "Lollipop"
        3 -> "Jelly Bean"
        else -> "Stage Unknown"
    }
}
