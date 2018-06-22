package de.droidcon.berlin2018.schedule.backend.data2018

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Xml

@Xml
data class SessionResult(
    @Element val items: List<SessionItem>
)
