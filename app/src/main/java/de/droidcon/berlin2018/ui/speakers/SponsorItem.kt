package de.droidcon.berlin2018.ui.speakers

import android.support.annotation.DrawableRes
import de.droidcon.berlin2018.R

data class SponsorItem(@DrawableRes val drawable: Int, val link : String)
object SponsorSectionTitle

val sponsors = listOf<Any>(
    SponsorSectionTitle,
    SponsorItem(R.drawable.sponsor_huawai, "http://www.huawei.com/en/?ic_medium=direct&ic_source=surlent"),
    SponsorItem(R.drawable.sponsor_android, "https://www.android.com/"),
    SponsorItem(R.drawable.sponsor_here, "https://www.here.com/en"),
    SponsorItem(R.drawable.sponsor_blockstack, "https://blockstack.org/"),
    SponsorItem(R.drawable.sponsor_sap, "https://www.sap.com/trends/mobile-technology.html"),
    SponsorItem(R.drawable.sponsor_wizy, "https://www.wizy.io/"),
    SponsorItem(R.drawable.sponsor_circlon, "https://www.wizy.io/"),
    SponsorItem(R.drawable.sponsor_alexa, "https://developer.amazon.com/de/alexa"),
    SponsorItem(R.drawable.sponsor_amazon, "https://developer.amazon.com/de/apps-and-games"),
    SponsorItem(R.drawable.sponsor_ebay, "https://ebaytech.berlin/"),
    SponsorItem(R.drawable.sponsor_greenspector, "https://greenspector.com"),
    SponsorItem(R.drawable.sponsor_gdg, "https://developers.google.com/"),
    SponsorItem(R.drawable.sponsor_edreams, "https://www.edreamsodigeo.com/"),
    SponsorItem(R.drawable.sponsor_n26, "https://next.n26.com/en/careers"),
    SponsorItem(R.drawable.sponsor_objectbox, "http://objectbox.io/"),
    SponsorItem(R.drawable.sponsor_pluralsight, "https://www.pluralsight.com/")
)
