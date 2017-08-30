package de.droidcon.berlin2017.search
import de.droidcon.berlin2017.model.Session

/**
 *
 *
 * @author Hannes Dorfmann
 */
data class SessionSearchableItem (
    val session : Session,
    val time : String?,
    val location : String?,
    val speakers : String?,
    val favorite : Boolean
) : SearchableItem