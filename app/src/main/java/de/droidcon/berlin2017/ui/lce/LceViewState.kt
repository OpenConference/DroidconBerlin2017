package de.droidcon.berlin2017.ui.lce

/**
 * State for screens that display LCE (Loading-Content-Error)
 *
 * @author Hannes Dorfmann
 */
sealed class LceViewState<T> {

  /**
   * Indicates that data is loading
   */
  class Loading<T> : LceViewState<T>()

  /**
   * Indicates that an error while loading data has occurred
   */
  data class Error<T>(val error: Throwable) : LceViewState<T>()

  /**
   * The content has been loaded successfully
   */
  data class Content<T>(val data: T) : LceViewState<T>()
}