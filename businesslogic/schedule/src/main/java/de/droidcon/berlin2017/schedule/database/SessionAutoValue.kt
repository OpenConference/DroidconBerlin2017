package de.droidcon.berlin2017.schedule.database

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import com.google.auto.value.AutoValue
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.model.Speaker
import org.threeten.bp.Instant

/**
 *
 * Contains the description of a Session
 * @author Hannes Dorfmann
 */
@AutoValue
abstract class SessionAutoValue : Session {

  /**
   * The session id
   */
  @NonNull
  override abstract fun id(): String

  /**
   * The title of the sessions
   */
  @Nullable
  override abstract fun title(): String?

  /**
   * The description of the speaker
   */
  @Nullable
  override abstract fun description(): String?

  /**
   * Optional tags for this session
   */
  @Nullable
  override abstract fun tags(): String?

  /**
   * Start date / time
   */
  @Nullable
  override abstract fun startTime(): Instant?

  /**
   * End date / time
   */
  @Nullable
  override abstract fun endTime(): Instant?

  /**
   * The location id
   */
  @Nullable
  override abstract fun locationId(): String?

  /**
   * The location name
   */
  @Nullable
  override abstract fun locationName(): String?

  /**
   * The speakers of this session
   */
  override abstract fun speakers(): List<Speaker>

  override abstract fun favorite(): Boolean

  companion object {

    fun create(id: String, title: String?, description: String?, tags: String?, start: Instant?,
        end: Instant?, locationId: String?, locationName: String?, favorite: Boolean,
        speakers: List<Speaker>): SessionAutoValue = AutoValue_SessionAutoValue(id, title,
        description, tags,
        start, end, locationId, locationName, speakers, favorite)
  }

}