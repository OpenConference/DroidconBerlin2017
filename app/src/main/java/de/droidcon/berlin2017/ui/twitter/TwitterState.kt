package de.droidcon.berlin2017.ui.twitter

/**
 * A workaround for this terrible Twitter SDK bug that causes ANR
 *
 * @author Hannes Dorfmann
 */
enum class TwitterState {
  IN_PROGRESS,
  READY
}