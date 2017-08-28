package de.droidcon.berlin2017.clock

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

/**
 * The clock used to get the time of the system
 *
 * @author Hannes Dorfmann
 */
interface Clock {

  /**
   * Get the current time
   */
  fun now(): Instant

  /**
   * Get the zone where the conference takes place
   */
  fun getZoneConferenceTakesPlace(): ZoneId

  /**
   * now in the timezone the conference takes place
   */
  fun nowInConferenceTimeZone(): LocalDateTime
}