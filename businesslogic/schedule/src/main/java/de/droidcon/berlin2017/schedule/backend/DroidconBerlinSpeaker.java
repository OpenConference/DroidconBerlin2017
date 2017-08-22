package de.droidcon.berlin2017.schedule.backend;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import de.droidcon.berlin2017.model.Speaker;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * @author Hannes Dorfmann
 */
@JsonObject class DroidconBerlinSpeaker implements Speaker {

  @JsonField(name = "uid") String id;
  @JsonField(name = "gn") String firstName;
  @JsonField(name = "sn") String lastName;
  @JsonField(name = "org") String company;
  @JsonField(name = "position") String jobTitle;
  @JsonField(name = "image") String profilePic;
  @JsonField(name = "description_short") String info;
  @JsonField(name = "links") List<DroidconBerlinLink> links;

  @Nullable @org.jetbrains.annotations.Nullable @Override public String profilePic() {
    return profilePic;
  }

  @Nullable @org.jetbrains.annotations.Nullable @Override public String link3() {
    return links != null && links.size() >= 1 ? links.get(0).url : null;
  }

  @Nullable @org.jetbrains.annotations.Nullable @Override public String link2() {
    return links != null && links.size() >= 2 ? links.get(1).url : null;
  }

  @Nullable @org.jetbrains.annotations.Nullable @Override public String link1() {
    return links != null && links.size() >= 3 ? links.get(2).url : null;
  }

  @Nullable @org.jetbrains.annotations.Nullable @Override public String jobTitle() {
    return jobTitle == null ? null : Html.fromHtml(jobTitle).toString();
  }

  @Nullable @org.jetbrains.annotations.Nullable @Override public String company() {
    return company == null ? null : Html.fromHtml(company).toString();
  }

  @Nullable @org.jetbrains.annotations.Nullable @Override public String info() {
    return info == null ? null : Html.fromHtml(info).toString();
  }

  @NonNull @NotNull @Override public String name() {
    return firstName + " " + lastName;
  }

  @NonNull @NotNull @Override public String id() {
    return id;
  }

  public DroidconBerlinSpeaker() {
  }

  public DroidconBerlinSpeaker(String id) {
    this.id = id;
  }
}
