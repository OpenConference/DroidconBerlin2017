package de.droidcon.berlin2018.schedule.backend.data2018.mapping

import com.google.auto.value.AutoValue
import de.droidcon.berlin2018.model.Speaker

@AutoValue
abstract class SimpleSpeaker : Speaker {
    companion object {
        fun create(
            id: String,
            name: String,
            info: String?,
            company: String?,
            jobTitle: String?,
            link1: String?,
            link2: String?,
            link3: String?,
            profilePic: String?
        ): SimpleSpeaker = AutoValue_SimpleSpeaker(
            id,
            name,
            info,
            company,
            jobTitle,
            link1,
            link2,
            link3,
            profilePic
        )
    }
}
