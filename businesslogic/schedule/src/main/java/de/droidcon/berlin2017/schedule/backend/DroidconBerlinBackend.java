package de.droidcon.berlin2017.schedule.backend;

import io.reactivex.Single;
import java.util.List;
import retrofit2.http.GET;

/**
 * @author Hannes Dorfmann
 */
public interface DroidconBerlinBackend {

  @GET("sessions.json") Single<List<DroidconBerlinSession>> getSessions();

  @GET("speakers.json") Single<List<DroidconBerlinSpeaker>> getSpeakers();

  @GET("rooms.json") Single<List<DroidconBerlinLocation>> getLocations();
}
