package de.droidcon.berlin2017.di

import android.content.Context
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient

/**
 *
 *
 * @author Hannes Dorfmann
 */
@Module
class PicassoModule(c: Context) {

  private val context = c.applicationContext


  @Provides
  fun providePicasso(okhttp: OkHttpClient) =
      Picasso.Builder(context).downloader(OkHttp3Downloader(okhttp)).build()

}