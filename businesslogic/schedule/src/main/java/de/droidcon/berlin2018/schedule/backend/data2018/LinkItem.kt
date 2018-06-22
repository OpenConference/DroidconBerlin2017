package de.droidcon.berlin2018.schedule.backend.data2018

import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "item")
data class LinkItem(
    @PropertyElement(name = "url") val url: String?,
    @PropertyElement(name = "title") val title: String?
)
