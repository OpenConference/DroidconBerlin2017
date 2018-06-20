package de.droidcon.berlin2018.ui.twitter

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.AdapterDataObserver
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.SearchTimeline
import com.twitter.sdk.android.tweetui.SearchTimeline.ResultType.RECENT
import com.twitter.sdk.android.tweetui.TimelineResult
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter
import de.droidcon.berlin2018.R
import de.droidcon.berlin2018.ui.applicationComponent
import de.droidcon.berlin2018.ui.gone
import de.droidcon.berlin2018.ui.twitter.TwitterState.READY
import de.droidcon.berlin2018.ui.visible
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber


/**
 * Shows tweets about #droidconDE
 *
 * @author Hannes Dorfmann
 */
class TwitterController : Controller() {


  private var searchTimeline: SearchTimeline? = null

  private var swipeRefresh: SwipeRefreshLayout? = null
  private var recyclerView: RecyclerView? = null
  private var loadingView: View? = null
  private var searchboxSpaceHolder: View? = null
  private var adapter: TweetTimelineRecyclerViewAdapter? = null

  private var initStateDisposable: Disposable? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {

    val twitterInitializer = applicationComponent().twitterInitilizer()

    val v = inflater.inflate(R.layout.controller_twitter, container, false) as ViewGroup

    searchboxSpaceHolder = v.findViewById(R.id.headerPlaceHolder)
    loadingView = v.findViewById(R.id.loading)
    swipeRefresh = v.findViewById(R.id.swipeRefresh)
    swipeRefresh!!.setProgressViewOffset(false, 0,
        resources!!.getDimensionPixelOffset(R.dimen.subheader_height_plus_searchbox))

    recyclerView = v.findViewById<RecyclerView>(R.id.recyclerView)
    recyclerView!!.layoutManager = LinearLayoutManager(container.context)


    val twitterReady = twitterInitializer.state.blockingFirst() == READY
    if (twitterReady) {
      initSearchTimeLineAndAdapter(container.context)
    } else {
      // wait until twitter ready
      initStateDisposable = twitterInitializer.state
          .filter { it == READY } // Wait until ready
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
            initSearchTimeLineAndAdapter(container.context);
          }, {
            // Should never be the case internally a subject is used
            Timber.e(it)
          }
          )

    }
    val tweetsToDisplay = adapter?.itemCount ?: 0
    if (!twitterReady || tweetsToDisplay == 0) { // tweetsToDisplay = 0 if not loaded yet
      loadingView!!.visible()
      swipeRefresh!!.gone()
      searchboxSpaceHolder!!.gone()
    }


    return v
  }

  override fun onDestroyView(view: View) {
    super.onDestroyView(view)
    initStateDisposable?.dispose()
    recyclerView?.adapter = null
    recyclerView?.layoutManager = null
    swipeRefresh?.setOnRefreshListener(null)
    recyclerView = null
    swipeRefresh = null
    loadingView = null
    searchboxSpaceHolder = null
    adapter = null
    searchTimeline = null
  }

  private fun initSearchTimeLineAndAdapter(context: Context) {

    searchTimeline = SearchTimeline.Builder()
        .query("#droidconDE")
        .resultType(RECENT)
        .build()


    adapter = TweetTimelineRecyclerViewAdapter.Builder(context)
        .setTimeline(searchTimeline)
        .setViewStyle(R.style.tw__TweetLightStyle)
        .build()

    recyclerView!!.adapter = adapter

    adapter!!.registerAdapterDataObserver(object : AdapterDataObserver() {
      override fun onChanged() {
        // Determine if finished loading
        // Hacky but you know ... Twitter SDK ... roll eyes

        if (view != null && loadingView!!.visibility == View.VISIBLE) {
          TransitionManager.beginDelayedTransition(view!! as ViewGroup)
          loadingView!!.gone()
          swipeRefresh!!.visible()
          searchboxSpaceHolder!!.visible()
        }
      }
    })

    swipeRefresh!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
      applicationComponent().analytics().trackTwitterRefresh()
      swipeRefresh?.isRefreshing = true
      adapter!!.refresh(object : Callback<TimelineResult<Tweet>>() {
        override fun success(result: Result<TimelineResult<Tweet>>) {
          swipeRefresh?.isRefreshing = false
        }

        override fun failure(exception: TwitterException) {
          Timber.e(exception)
          swipeRefresh?.isRefreshing = false
          if (swipeRefresh != null)
            Snackbar.make(swipeRefresh!!, R.string.error_unknown, Snackbar.LENGTH_SHORT).show()
        }
      })
    })

  }
}
