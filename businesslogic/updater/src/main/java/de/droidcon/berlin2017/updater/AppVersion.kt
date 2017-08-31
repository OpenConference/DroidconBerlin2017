package de.droidcon.berlin2017.updater

/**
 * Holds all required information about the app version
 *
 * @author Hannes Dorfmann
 */
data class AppVersion(
    val appPublished: Boolean,
    val newerAppVersionAvailable: Boolean
)