package de.droidcon.berlin2017.schedule.backend

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.Instant
import org.threeten.bp.format.DateTimeFormatter

/**
 * Logan Square json parser type converter for Dates ins ISO format
 *
 * @author Hannes Dorfmann
 */
class InstantIsoTypeConverter() {


  @ToJson
  fun convertToString(o: Instant?): String? = if (o == null) {
    null
  } else {
    val timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
    timeFormatter.format(o)

  }

  @FromJson
  fun getFromString(str: String?): Instant? = if (str == null) {
    null
  } else {
    val timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
    val accessor = timeFormatter.parse(str);
    Instant.from(accessor)
  }
}