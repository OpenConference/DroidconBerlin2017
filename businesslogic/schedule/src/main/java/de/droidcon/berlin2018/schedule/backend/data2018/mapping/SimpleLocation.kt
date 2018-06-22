package de.droidcon.berlin2018.schedule.backend.data2018.mapping

import com.google.auto.value.AutoValue
import de.droidcon.berlin2018.model.Location

@AutoValue
abstract class SimpleLocation : Location {
    companion object {
        fun create(id : String, name : String) : Location = AutoValue_SimpleLocation(id, name)
    }
}
