package de.droidcon.berlin2017.ui.twitter

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
import de.droidcon.berlin2017.R
import de.droidcon.berlin2017.ui.applicationComponent
import de.droidcon.berlin2017.ui.gone
import de.droidcon.berlin2017.ui.visible
import timber.log.Timber


/**
 * Shows tweets about #droidconDE
 *
 * @author Hannes Dorfmann
 */
class TwitterController : Controller() {


  val searchTimeline = SearchTimeline.Builder()
      .query("#droidconDE")
      .resultType(RECENT)
      .build()

  private var swipeRefresh: SwipeRefreshLayout? = null
  private var recyclerView: RecyclerView? = null
  private var loadingView: View? = null
  private var searchboxSpaceHolder: View? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {


    val v = inflater.inflate(R.layout.controller_twitter, container, false) as ViewGroup

    val adapter = TweetTimelineRecyclerViewAdapter.Builder(container.context)
        .setTimeline(searchTimeline)
        .setViewStyle(R.style.tw__TweetLightStyle)
        .build()

    adapter.registerAdapterDataObserver(object : AdapterDataObserver() {
      override fun onChanged() {
        // Determine if finished loading
        // Hacky but you know ... Twitter SDK ... roll eyes
        Timber.d("Timeline loading?")
        if (loadingView!!.visibility == View.VISIBLE) {
          TransitionManager.beginDelayedTransition(v)
          loadingView!!.gone()
          swipeRefresh!!.visible()
          searchboxSpaceHolder!!.visible()
        }
      }
    })

    searchboxSpaceHolder = v.findViewById(R.id.headerPlaceHolder)
    loadingView = v.findViewById(R.id.loading)
    swipeRefresh = v.findViewById(R.id.swipeRefresh)
    swipeRefresh!!.setProgressViewOffset(false, 0,
        resources!!.getDimensionPixelOffset(R.dimen.subheader_height_plus_searchbox))


    val rv = v.findViewById<RecyclerView>(R.id.recyclerView)
    rv.layoutManager = LinearLayoutManager(container.context)
    rv.adapter = adapter

    recyclerView = rv

    swipeRefresh!!.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
      applicationComponent().analytics().trackTwitterRefresh()
      swipeRefresh?.isRefreshing = true
      adapter.refresh(object : Callback<TimelineResult<Tweet>>() {
        override fun success(result: Result<TimelineResult<Tweet>>) {
          swipeRefresh?.isRefreshing = false
        }

        override fun failure(exception: TwitterException) {
          Timber.e(exception)
          swipeRefresh?.isRefreshing = false
          Snackbar.make(v, R.string.error_unknown, Snackbar.LENGTH_SHORT).show()
        }
      })
    })

    if (adapter.itemCount == 0) {
      loadingView!!.visible()
      swipeRefresh!!.gone()
      searchboxSpaceHolder!!.gone()
    }

    return v
  }

  override fun onDestroyView(view: View) {
    super.onDestroyView(view)
    recyclerView?.adapter = null
    recyclerView?.layoutManager = null
    swipeRefresh?.setOnRefreshListener(null)
    recyclerView = null
    swipeRefresh = null
    loadingView = null
    searchboxSpaceHolder = null
  }
}