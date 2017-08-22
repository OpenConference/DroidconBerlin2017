package de.droidcon.berlin2017.schedule.backend;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import de.droidcon.berlin2017.model.Location;
import org.jetbrains.annotations.NotNull;

/**
 * @author Hannes Dorfmann
 */
@JsonObject class DroidconBerlinLocation implements Location {

  @JsonField(name = "title") String name;
  @JsonField(name = "nid") String id;

  @NotNull @Override public String name() {
    return name;
  }

  @NotNull @Override public String id() {
    return id;
  }

  public DroidconBerlinLocation() {
  }
}
