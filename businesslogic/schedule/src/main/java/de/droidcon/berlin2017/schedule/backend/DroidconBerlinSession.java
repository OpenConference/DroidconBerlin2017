package de.droidcon.berlin2017.schedule.backend;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import de.droidcon.berlin2017.model.Session;
import de.droidcon.berlin2017.model.Speaker;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.Instant;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * @author Hannes Dorfmann
 */
@JsonObject class DroidconBerlinSession implements Session {

  @JsonField String title;
  @JsonField(name = "nid") String id;
  @JsonField(name = "abstract") String description;
  @JsonField(name = "speaker_uids") List<String> speakerIds;
  @JsonField(name = "room_id") List<String> roomIds;
  @JsonField(name = "start_iso") List<String> startTimes;
  @JsonField(name = "end_iso") List<String> endTimes;

  private Instant start;
  private Instant end;
  private ArrayList<Speaker> speakers;

  @NonNull @Override public String id() {
    return id;
  }

  @Nullable @org.jetbrains.annotations.Nullable @Override public String title() {
    return title;
  }

  @Nullable @org.jetbrains.annotations.Nullable @Override public String description() {
    return description == null ? null : Html.fromHtml(description).toString();
  }

  @Nullable @Override public String tags() {
    return null;
  }

  @Nullable @Override public String locationId() {
    if (roomIds != null && !roomIds.isEmpty()) {
      return roomIds.get(0);
    }
    return null;
  }

  @Nullable @Override public String locationName() {
    return null;
  }

  @Nullable @Override public Instant startTime() {
    if (start == null) {
      if (startTimes == null || startTimes.isEmpty()) {
        return null;
      }
      start = Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(startTimes.get(0)));
    }
    return start;
  }

  @Nullable @Override public Instant endTime() {

    if (end == null) {
      if (endTimes == null || endTimes.isEmpty()) {
        return null;
      }
      end = Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(endTimes.get(0)));
    }
    return end;
  }

  @Nullable @Override public List<Speaker> speakers() {
    if (speakers == null) {
      if (speakerIds == null) {
        return null;
      }

      speakers = new ArrayList<>(speakerIds.size());
      for (String speakerId : speakerIds) {
        speakers.add(new DroidconBerlinSpeaker(speakerId));
      }
    }

    return speakers;
  }

  @NonNull @Override public boolean favorite() {
    return false;
  }

  public DroidconBerlinSession() {
  }
}
