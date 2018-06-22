package de.droidcon.berlin2018.schedule.backend.data2018.mapping

import com.google.auto.value.AutoValue
import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.model.Speaker
import org.threeten.bp.Instant

@AutoValue
abstract class SimpleSession : Session {
    companion object {
        fun create(
            id: String,
            title: String?,
            description: String?,
            tags: String?,
            locationId: String?,
            locationName: String?,
            startTime: Instant?,
            endTime: Instant?,
            speakers: List<Speaker>?,
            favorite: Boolean
        ): Session = AutoValue_SimpleSession(
            id,
            title,
            description,
            tags,
            locationId,
            locationName,
            startTime,
            endTime,
            speakers,
            favorite
        )
    }
}
