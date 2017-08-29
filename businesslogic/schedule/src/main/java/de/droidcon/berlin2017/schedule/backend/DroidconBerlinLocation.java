package de.droidcon.berlin2017.schedule.backend;

import com.squareup.moshi.Json;
import de.droidcon.berlin2017.model.Location;
import org.jetbrains.annotations.NotNull;

/**
 * @author Hannes Dorfmann
 */
class DroidconBerlinLocation implements Location {

  @Json(name = "title") String name;
  @Json(name = "nid") String id;

  @NotNull @Override public String name() {
    return name;
  }

  @NotNull @Override public String id() {
    return id;
  }

  public DroidconBerlinLocation() {
  }
}
