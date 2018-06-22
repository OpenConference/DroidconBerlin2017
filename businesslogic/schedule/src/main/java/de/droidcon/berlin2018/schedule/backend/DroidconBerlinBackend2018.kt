package de.droidcon.berlin2018.schedule.backend

import de.droidcon.berlin2018.schedule.backend.data2018.SessionResult
import de.droidcon.berlin2018.schedule.backend.data2018.SpeakerResult
import io.reactivex.Single
import retrofit2.http.GET

interface DroidconBerlinBackend2018 {
    @GET("rest/speakers.xml")
    fun getSpeakers(): Single<SpeakerResult>

    @GET("rest/sessions.xml")
    fun getSession(): Single<SessionResult>
}
