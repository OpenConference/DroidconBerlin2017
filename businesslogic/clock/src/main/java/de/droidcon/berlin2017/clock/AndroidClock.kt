package de.droidcon.berlin2017.clock

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId


/**
 * The real android system clock
 *
 * @author Hannes Dorfmann
 */
class AndroidClock : Clock {

  private val conferenceZone = ZoneId.of("Europe/Berlin")

  override fun now(): Instant = Instant.now()

  override fun getZoneConferenceTakesPlace(): ZoneId = conferenceZone

  override fun nowInConferenceTimeZone(): LocalDateTime = LocalDateTime.now(conferenceZone)
}