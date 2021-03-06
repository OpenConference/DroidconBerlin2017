package de.droidcon.berlin2018.schedule.backend

import de.droidcon.berlin2018.model.Location
import de.droidcon.berlin2018.model.Session
import de.droidcon.berlin2018.model.Speaker
import de.droidcon.berlin2018.schedule.backend.data2018.SessionItem
import de.droidcon.berlin2018.schedule.backend.data2018.SessionResult
import de.droidcon.berlin2018.schedule.backend.data2018.SpeakerItem
import de.droidcon.berlin2018.schedule.backend.data2018.mapping.SimpleLocation
import de.droidcon.berlin2018.schedule.backend.data2018.mapping.SimpleSession
import de.droidcon.berlin2018.schedule.backend.data2018.mapping.SimpleSpeaker
import io.reactivex.Single
import io.reactivex.functions.BiFunction


/**
 * [BackendScheduleAdapter] for droidcon Berlin backend
 *
 * @author Hannes Dorfmann
 */
public class DroidconBerlinBackendScheduleAdapter2018(
    private val backend: DroidconBerlinBackend2018
) : BackendScheduleAdapter {

    override fun getSpeakers(): Single<BackendScheduleResponse<Speaker>> =
        backend.getSpeakers().map { speakerResult ->
            val list: List<Speaker> = speakerResult.items.map { it.toSpeaker() }
            BackendScheduleResponse.dataChanged(list)
        }

    override fun getLocations(): Single<BackendScheduleResponse<Location>> =
        backend.getSession().map { result: SessionResult? ->
            if (result == null)
                BackendScheduleResponse.dataChanged(emptyList())
            else {
                val locations: List<Location> =
                    result.items.map {
                        SimpleLocation.create(
                            it.roomName,
                            it.roomName
                        )
                    } // TODO roomId is fucked up on backend
                        .distinct()
                        .toList()
                BackendScheduleResponse.dataChanged(locations)
            }
        }

    override fun getSessions(): Single<BackendScheduleResponse<Session>> {
        val sessionsResult = backend.getSession()
        val spakersMap = backend.getSpeakers().map {
            it.items.map { it.toSpeaker() }.associateBy { it.id() }
        }

        val sessions: Single<List<Session>> =
            Single.zip(sessionsResult, spakersMap, BiFunction { sessions, speakersMap ->
                sessions.items.map { it.toSession(speakersMap) }
            })

        return sessions.map {
            BackendScheduleResponse.dataChanged(it)
        }
    }


    private fun SpeakerItem.toSpeaker(): Speaker = SimpleSpeaker.create(
        id = id,
        name = "$firstname $lastname",
        company = company,
        info = description,
        jobTitle = position,
        profilePic = imageUrl,
        link1 = if (links != null && links.size >= 1) links[0].url else null,
        link2 = if (links != null && links.size >= 2) links[1].url else null,
        link3 = if (links != null && links.size >= 3) links[2].url else null
    )

    private fun SessionItem.toSession(speakers: Map<String, Speaker>): Session {
        return SimpleSession.create(
            id = id,
            title = title,
            description = description,
            favorite = false,
            locationId = roomName, // TODO roomId is fucked up on backend site.
            locationName = roomName,
            speakers = when (id) {
                "3858" -> listOf(speakers["237"]!!, speakers["1135"]!!)// MVI
                "3785" -> listOf(speakers["1144"]!!, speakers["347"]!!)// Code sharing is caring
                "3949" -> listOf(
                    speakers["1217"]!!,
                    speakers["276"]!!
                )// Everything is better with(out) Bluetooth
                "3859" -> listOf(speakers["127"]!!, speakers["304"]!!)// The build side of an app
                "5302" -> listOf(
                    speakers["1345"]!!,
                    speakers["1347"]!!,
                    speakers["1348"]!!,
                    speakers["1349"]!!
                ) // Manage android without an app
                "5309" -> listOf(
                    speakers["1345"]!!,
                    speakers["1347"]!!,
                    speakers["1348"]!!,
                    speakers["1349"]!!
                ) // Manage android api hands on
                "5307" -> listOf(
                    speakers["1354"]!!,
                    speakers["1355"]!!
                ) // Sharing a success story

                else -> listOf(speakers[speakerIds]!!)
            },
            tags = category,
            startTime = startDate,
            endTime = endDate
        )
    }
}
