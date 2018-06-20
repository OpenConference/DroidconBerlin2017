package de.droidcon.berlin2018.di

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import de.droidcon.berlin2018.BuildConfig
import de.droidcon.berlin2018.schedule.backend.BackendScheduleAdapter
import de.droidcon.berlin2018.schedule.backend.DroidconBerlinBackend
import de.droidcon.berlin2018.schedule.backend.DroidconBerlinBackendScheduleAdapter
import de.droidcon.berlin2018.schedule.backend.HtmlStringTypeAdapter
import de.droidcon.berlin2018.schedule.backend.InstantIsoTypeConverter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * @author Hannes Dorfmann
 */
@Module
open class NetworkModule(context: Context) {

  private val retrofit: Retrofit
  private val okHttp: OkHttpClient
  private val backendAdapter: BackendScheduleAdapter
  private val backend: DroidconBerlinBackend

  init {
    val builder = OkHttpClient.Builder()
        .cache(okhttp3.Cache(context.cacheDir, 48 * 1024 * 1024))

    if (BuildConfig.DEBUG) {
      val logging = HttpLoggingInterceptor()
      logging.level = Level.HEADERS
      builder.addInterceptor(logging)
    }
    okHttp = builder.build()

    val moshi = Moshi.Builder()
        .add(HtmlStringTypeAdapter.newFactory())
        .add(InstantIsoTypeConverter())
        .build()

    retrofit = Retrofit.Builder()
        .client(okHttp)
        .baseUrl("http://droidcon.de/rest/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()


    backend = retrofit.create(DroidconBerlinBackend::class.java)
    backendAdapter = DroidconBerlinBackendScheduleAdapter(backend)
  }

  @Provides
  fun provideOkHttp(): OkHttpClient = okHttp

  @Provides
  fun provideBackendAdapter(): BackendScheduleAdapter = backendAdapter

}
