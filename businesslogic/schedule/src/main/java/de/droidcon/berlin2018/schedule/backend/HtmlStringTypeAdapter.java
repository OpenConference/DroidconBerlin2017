package de.droidcon.berlin2018.schedule.backend;

import android.text.Html;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

public final class HtmlStringTypeAdapter extends JsonAdapter<String> {
  private final JsonAdapter<String> delegate;

  private HtmlStringTypeAdapter(JsonAdapter<String> delegate) {
    this.delegate = delegate;
  }

  @Override public String fromJson(JsonReader reader) throws IOException {
    // Read the value first so that the reader will be in a known state even if there's an
    // exception. Otherwise it may be awkward to recover: it might be between calls to
    // beginObject() and endObject() for example.
    Object jsonValue = reader.readJsonValue();

    // Use the delegate to convert the JSON value to the target type.

      return Html.fromHtml(delegate.fromJsonValue(jsonValue)).toString();
  }

  @Override public void toJson(JsonWriter writer, String value) throws IOException {
    delegate.toJson(writer, value);
  }

  public static Factory newFactory() {
    final Class<String> type = String.class;

    return new Factory() {
      @Override public JsonAdapter<?> create(
          Type requestedType, Set<? extends Annotation> annotations, Moshi moshi) {
        if (type != requestedType) return null;
        JsonAdapter<String> delegate = moshi.nextAdapter(this, type, annotations);
        return new HtmlStringTypeAdapter(delegate);
      }
    };
  }
}
