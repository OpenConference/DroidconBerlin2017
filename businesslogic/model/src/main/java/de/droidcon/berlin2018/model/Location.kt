package de.droidcon.berlin2018.model


/**
 * Represents a location where a session takes place
 *
 * @author Hannes Dorfmann
 */
interface Location {

  /**
   * The id of the location
   */
  fun id(): String

  /**
   * The name of the location
   */
  fun name(): String
}
