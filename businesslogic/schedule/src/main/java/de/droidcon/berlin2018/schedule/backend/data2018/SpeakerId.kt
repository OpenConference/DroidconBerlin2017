package de.droidcon.berlin2018.schedule.backend.data2018

import com.tickaroo.tikxml.annotation.TextContent
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "item")
data class SpeakerId(
    @TextContent val id: String
)
