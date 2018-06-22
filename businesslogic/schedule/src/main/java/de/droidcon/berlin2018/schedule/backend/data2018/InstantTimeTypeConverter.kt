package de.droidcon.berlin2018.schedule.backend.data2018

import com.tickaroo.tikxml.TypeConverter
import de.droidcon.berlin2018.schedule.backend.InstantIsoTypeConverter
import org.threeten.bp.Instant

class InstantTimeTypeConverter : TypeConverter<Instant> {
    private val converter = InstantIsoTypeConverter()
    override fun write(value: Instant?): String? = converter.convertToString(value)

    override fun read(value: String?): Instant? = converter.getFromString(value)
}
