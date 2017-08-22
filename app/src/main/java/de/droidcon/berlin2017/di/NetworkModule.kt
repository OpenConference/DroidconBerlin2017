package de.droidcon.berlin2017.di

import android.content.Context
import com.github.aurae.retrofit2.LoganSquareConverterFactory
import dagger.Module
import dagger.Provides
import de.droidcon.berlin2017.schedule.backend.BackendScheduleAdapter
import de.droidcon.berlin2017.schedule.backend.DroidconBerlinBackend
import de.droidcon.berlin2017.schedule.backend.DroidconBerlinBackendScheduleAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import javax.inject.Singleton

/**
 * @author Hannes Dorfmann
 */
@Module
open class NetworkModule(private val context: Context) {

  private val retrofit: Retrofit
  private val okHttp: OkHttpClient
  private val backendAdapter: BackendScheduleAdapter
  private val backend: DroidconBerlinBackend

  init {
    okHttp = OkHttpClient.Builder().cache(okhttp3.Cache(context.cacheDir, 48 * 1024 * 1024))
        .build()

    retrofit = Retrofit.Builder()
        .client(okHttp)
        .baseUrl("http://droidcon.de/rest/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(LoganSquareConverterFactory.create())
        .build()


    backend = retrofit.create(DroidconBerlinBackend::class.java)
    backendAdapter = DroidconBerlinBackendScheduleAdapter(backend)
  }

  @Provides
  @Singleton
  fun provideOkHttp(): OkHttpClient = okHttp

  @Provides
  @Singleton
  fun provideBackendAdapter(): BackendScheduleAdapter = backendAdapter

}