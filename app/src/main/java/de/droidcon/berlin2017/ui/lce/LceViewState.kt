package de.droidcon.berlin2017.ui.lce

import java.io.PrintWriter
import java.io.StringWriter

/**
 * State for screens that display LCE (Loading-Content-Error)
 *
 * @author Hannes Dorfmann
 */
sealed class LceViewState<T> {

  /**
   * Indicates that data is loading
   */
  class Loading<T> : LceViewState<T>() {
    override fun toString(): String = "LceViewState.Loading"
  }

  /**
   * Indicates that an error while loading data has occurred
   */
  data class Error<T>(val error: Throwable) : LceViewState<T>(){
    override fun toString(): String {
      val sw = StringWriter()
      error.printStackTrace(PrintWriter(sw))
      return super.toString() +"\n${sw.toString()}"
    }
  }

  /**
   * The content has been loaded successfully
   */
  data class Content<T>(val data: T) : LceViewState<T>()
}