package de.droidcon.berlin2018.schedule.backend.data2018

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "item")
data class SpeakerItem(
    @PropertyElement(name = "uid") val id: String,
    @PropertyElement(name = "gn") val firstname: String,
    @PropertyElement(name = "sn") val lastname: String,
    @PropertyElement(name = "org") val company: String?,
    @PropertyElement(name = "org_uri") val companyUrl: String?,
    @PropertyElement(name = "position") val position: String?,
    @PropertyElement(name = "image") val imageUrl: String,
    @PropertyElement(name = "description_short") val description: String,
    @Path("links") @Element(name = "item") val links: List<LinkItem>?
)
