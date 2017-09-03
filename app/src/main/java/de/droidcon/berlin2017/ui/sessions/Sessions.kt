package de.droidcon.berlin2017.ui.sessions

import android.support.v7.util.DiffUtil.DiffResult
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.model.Speaker
import org.threeten.bp.LocalDateTime

/**
 * A data class that represents all needed information to display a list of sessions
 *
 * @author Hannes Dorfmann
 */
data class Sessions(
    val scrollTo: Int?,
    val sessions: List<SchedulePresentationModel>,
    val diffResult: DiffResult?
)

/**
 * A Presentation Model ready to be used in UI.
 * Presentation model is optimized for UI
 */
sealed class SchedulePresentationModel {

  /**
   * The date this event happens.
   * Mainly used for sorting
   */
  abstract val date: LocalDateTime

  abstract val fastScrollInfo: String

  /**
   * Represents a session
   */
  data class SessionPresentationModel(
      val id: String,
      val title: String?,
      val locationName: String?,
      val time: String?,
      override val fastScrollInfo: String,
      val favorite: Boolean,
      val speakers: List<Speaker>,
      val speakerNames: String,
      override val date: LocalDateTime,
      val session: Session
  ) : SchedulePresentationModel()


  /**
   * Represents a dayString that contains [TimeSlotPresentationModel] and [SessionPresentationModel]
   */
  data class DayPresentationModel(
      val dayString: String,
      val dateString: String,
      override val date: LocalDateTime,
      override val fastScrollInfo: String
  ) : SchedulePresentationModel()

  /**
   * A visual divider for timeslots (like 10 AM)
   */
  data class TimeSlotDividerPresentationModel(
      val timeStr : String,
      override val date: LocalDateTime,
      override val fastScrollInfo: String
  ) : SchedulePresentationModel()
}