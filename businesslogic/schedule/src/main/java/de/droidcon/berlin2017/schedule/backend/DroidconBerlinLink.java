package de.droidcon.berlin2017.schedule.backend;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * @author Hannes Dorfmann
 */
@JsonObject class DroidconBerlinLink {
  @JsonField String url;

  public DroidconBerlinLink() {
  }
}
