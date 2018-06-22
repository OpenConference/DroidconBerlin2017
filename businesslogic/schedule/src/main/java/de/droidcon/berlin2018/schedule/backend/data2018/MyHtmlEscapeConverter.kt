package de.droidcon.berlin2018.schedule.backend.data2018

import android.text.Html
import com.tickaroo.tikxml.converter.htmlescape.HtmlEscapeStringConverter

class MyHtmlEscapeConverter : HtmlEscapeStringConverter() {
    override fun read(s: String?): String? {
        return if (s == null) null
        else Html.fromHtml(super.read(s)).toString()
    }

    override fun write(s: String?): String {
        return super.write(s)
    }
}
