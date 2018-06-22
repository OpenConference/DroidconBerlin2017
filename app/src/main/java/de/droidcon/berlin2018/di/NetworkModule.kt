package de.droidcon.berlin2018.di

import android.content.Context
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import de.droidcon.berlin2018.BuildConfig
import de.droidcon.berlin2018.schedule.backend.BackendScheduleAdapter
import de.droidcon.berlin2018.schedule.backend.DroidconBerlinBackend2018
import de.droidcon.berlin2018.schedule.backend.DroidconBerlinBackendScheduleAdapter2018
import de.droidcon.berlin2018.schedule.backend.data2018.InstantTimeTypeConverter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.threeten.bp.Instant
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


/**
 * @author Hannes Dorfmann
 */
@Module
open class NetworkModule(context: Context) {

    private val retrofit: Retrofit
    private val okHttp: OkHttpClient
    private val backendAdapter: BackendScheduleAdapter

    init {
        val builder = OkHttpClient.Builder()
            .cache(okhttp3.Cache(context.cacheDir, 48 * 1024 * 1024))

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = Level.HEADERS
            builder.addInterceptor(logging)
        }
        okHttp = builder.build()

        /*
      val moshi = Moshi.Builder()
          .add(HtmlStringTypeAdapter.newFactory())
          .add(InstantIsoTypeConverter())
          .build()

  */
        retrofit = Retrofit.Builder()
            .client(okHttp)
            .baseUrl("https://cfp.droidcon.de")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                TikXmlConverterFactory.create(
                    TikXml.Builder()
                        .exceptionOnUnreadXml(false)
                        .addTypeConverter(Instant::class.java, InstantTimeTypeConverter())
                        .build()
                )
            )
            .build()


        val backend = retrofit.create(DroidconBerlinBackend2018::class.java)
        backendAdapter = DroidconBerlinBackendScheduleAdapter2018(backend)
    }

    @Provides
    fun provideOkHttp(): OkHttpClient = okHttp

    @Provides
    fun provideBackendAdapter(): BackendScheduleAdapter = backendAdapter

}
