package de.droidcon.berlin2018.schedule.backend.data2018

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import org.threeten.bp.Instant

@Xml(name = "item")
data class SessionItem(
    @PropertyElement val title: String,
    @Path("start_iso") @PropertyElement(name = "item") val startDate: Instant?,
    @Path("end_iso") @PropertyElement(name = "item") val endDate: Instant?,
    @Path("room_id") @PropertyElement(name = "item") val roomId: String,
    @Path("room") @PropertyElement(name = "item") val roomName: String,
    @Element(name = "speaker_uids") val speakerIds: List<SpeakerId>?,
    @PropertyElement(name = "abstract") val description: String,
    @PropertyElement(name = "nid") val id: String
)
