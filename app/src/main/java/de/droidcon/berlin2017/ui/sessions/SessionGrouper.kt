package de.droidcon.berlin2017.ui.sessions

import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.model.Speaker
import de.droidcon.berlin2017.ui.sessions.SchedulePresentationModel.DayPresentationModel
import de.droidcon.berlin2017.ui.sessions.SchedulePresentationModel.SessionPresentationModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle.SHORT

private val dayInWeekShort = DateTimeFormatter.ofPattern("EEE")
private val timeFormatter = DateTimeFormatter.ofLocalizedTime(SHORT)
private val dayInWeekLong = DateTimeFormatter.ofPattern("EEEE")
private val dateShortFormatter = DateTimeFormatter.ofLocalizedDate(SHORT)

interface SessionGrouper {
  fun groupSessions(sessions: List<Session>,
      zoneConferenceTakesPlace: ZoneId): List<SchedulePresentationModel>
}

class PhoneSessionGrouper() : SessionGrouper {
  override fun groupSessions(sessions: List<Session>,
      zoneConferenceTakesPlace: ZoneId): List<SchedulePresentationModel> {

    val fallbackStartDateIfDateNotSet: LocalDateTime = LocalDateTime.now(
        zoneConferenceTakesPlace).plusYears(5)

    val groupedByDay = sessions.groupBy {
      val startTime = it.startTime()
      if (startTime == null)
        fallbackStartDateIfDateNotSet.toLocalDate()
      else
        LocalDateTime.ofInstant(startTime, zoneConferenceTakesPlace).toLocalDate()
    }
        .toList()
        .sortedBy { (startDate, _) -> startDate }


    return groupedByDay.flatMap { (day, sessions) ->

      // Does the following:
      // 1. Adds Day Header
      // 2. Adds TimeSlot dividers
      // 3. Transforms sessions into Presentation Model

      val result = ArrayList<SchedulePresentationModel>()
      if (day == null) {
        result.add(
            // Put at the very end of the list
            DayPresentationModel(
                dayString = "Unknown",
                dateString = "Date not set yet",
                date = LocalDateTime.now(zoneConferenceTakesPlace).plusYears(10),
                fastScrollInfo = "Unknown"
            )
        )

        result += sessions.toSchedulePresentationModel(zoneConferenceTakesPlace,
            fallbackStartDateIfDateNotSet)

      } else {
        // DateTime available for sessions
        result.add(DayPresentationModel(
            dayString = dayInWeekLong.format(day),
            dateString = dateShortFormatter.format(day),
            date = day.atStartOfDay(),
            fastScrollInfo = dayInWeekLong.format(day)
        ))

        // Group Sessions by time (creates "time slots")
        // Precondition: Session has a start time holds because start time already checked for grouping by day
        val timeSlots = sessions.groupBy { it.startTime() }.toList().sortedBy { (startDateTime, _) -> startDateTime }
        timeSlots.forEachIndexed { i, (startDateTime, sessionInTimeSlot) ->
          /*
          if (i > 0) {
            val startZoned = LocalDateTime.ofInstant(startDateTime, zoneConferenceTakesPlace)
            result.add(TimeSlotDividerPresentationModel(
                date = startZoned,
                fastScrollInfo = dayInWeekShort.format(startZoned) + " " + timeFormatter.format(
                    startZoned)
            ))
          }
          */
          result += sessionInTimeSlot.toSchedulePresentationModel(zoneConferenceTakesPlace,
              fallbackStartDateIfDateNotSet)
        }
      }
      result
    }
  }
}


/**
 * Transforms a [Session] into a [SessionPresentationModel]
 */
private fun List<Session>.toSchedulePresentationModel(zoneConferenceTakesPlace: ZoneId,
    fallbackStartDateIfDateNotSet: LocalDateTime): List<SessionPresentationModel> = map {

  val start = if (it.startTime() == null) null else LocalDateTime.ofInstant(it.startTime(),
      zoneConferenceTakesPlace)

  SessionPresentationModel(
      id = it.id(),
      title = it.title(),
      fastScrollInfo = if (start == null) "Unknown" else dayInWeekShort.format(
          start) + " " + timeFormatter.format(start),
      locationName = it.locationName(),
      speakers = it.speakers(),
      date = start ?: fallbackStartDateIfDateNotSet,
      favorite = it.favorite(),
      speakerNames = it.speakers().speakerNames(),
      time = it.shortTime(zoneConferenceTakesPlace, false),
      session = it
  )
}


fun List<Speaker>.speakerNames(): String {
  return if (size == 1) this[0].name() else this.foldIndexed(
      StringBuilder()) { i, builder, speaker ->
    if (i == this.size - 1)
      builder.append(" & ")
    else if (i > 0) builder.append(", ")

    builder.append(speaker.name())

  }.toString()

}

fun Session.shortTime(zoneConferenceTakesPlace: ZoneId, dayOfWeek: Boolean): String? {

  val start = if (this.startTime() == null) null else LocalDateTime.ofInstant(this.startTime(),
      zoneConferenceTakesPlace)
  val end = if (this.endTime() == null) null else LocalDateTime.ofInstant(this.endTime(),
      zoneConferenceTakesPlace)


  val time = if (start == null) null else {
    if (end != null)
      "${timeFormatter.format(start)} - ${timeFormatter.format(end)}"
    else
      timeFormatter.format(start)
  }

  return if (dayOfWeek && start != null)
    dayInWeekShort.format(start) + ", " + time
  else
    time
}
