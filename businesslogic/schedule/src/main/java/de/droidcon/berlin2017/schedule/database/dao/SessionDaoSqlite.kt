package de.droidcon.berlin2017.schedule.database.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.hannesdorfmann.sqlbrite.dao.Dao
import com.openconference.model.database.SessionDateTimeComparator
import com.squareup.sqlbrite.BriteDatabase
import de.droidcon.berlin2017.model.Session
import de.droidcon.berlin2017.schedule.database.putOrNull
import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Observable


/**
 * Data-Access-Object for [com.droidcon.model.Session]
 *
 * @author Hannes Dorfmann
 */
open class SessionDaoSqlite : SessionDao, Dao() {


  companion object {
    const val TABLE = "Session"
    const val COL_ID = "id"
    const val COL_TITLE = "title"
    const val COL_DESCRIPTION = "description"
    const val COL_TAGS = "tags"
    const val COL_START_TIME = "startTime"
    const val COL_END_TIME = "endTime"
    const val COL_FAVORITE = "favorite"

    const val COL_LOCATION_ID = "locationId"
    const val COL_LOCATION_NAME = "locationName"

    const val COL_SPEAKER_ID = "speakerId"
    const val COL_SPEAKER_NAME = "speakerName"
    const val COL_SPEAKER_PIC = "speakerPicture"
    const val COL_SPEAKER_COMPANY = "speakerCompany"
    const val COL_SPEAKER_JOBTITLE = "speakerJobTitle"

  }

  /**
   * Internal sql table for n:m relation between speaker and session
   */
  private object GiveTalk {
    const val TABLE = "GiveTalk"
    const val COL_SESSION_ID = "sessionId"
    const val COL_SPEAKER_ID = "speakerId"
  }

  private val sortByStartDate = SessionDateTimeComparator()

  override fun createTable(database: SQLiteDatabase) {

    CREATE_TABLE(TABLE,
        "${COL_ID} VARCHAR(40) PRIMARY KEY NOT NULL",
        "${COL_TITLE} TEXT NOT NULL",
        "${COL_DESCRIPTION} TEXT",
        "${COL_START_TIME} BIGINTEGER",
        "${COL_END_TIME} BIGINTEGER",
        "${COL_TAGS} TEXT",
        "$COL_LOCATION_ID VARCHAR(20)",
        "$COL_FAVORITE BOOLEAN DEFAULT FALSE",
        "FOREIGN KEY($COL_LOCATION_ID) REFERENCES ${LocationDaoSqlite.TABLE}(${LocationDaoSqlite.COL_ID}) ON DELETE SET NULL"
    ).execute(database)

    CREATE_TABLE(GiveTalk.TABLE,
        "${GiveTalk.COL_SESSION_ID} VARCHAR(40) NOT NULL",
        "${GiveTalk.COL_SPEAKER_ID} VARCHAR(20) NOT NULL",
        "PRIMARY KEY(${GiveTalk.COL_SESSION_ID}, ${GiveTalk.COL_SPEAKER_ID})",
        "FOREIGN KEY(${GiveTalk.COL_SESSION_ID}) REFERENCES ${TABLE}($COL_ID) ON DELETE CASCADE",
        "FOREIGN KEY(${GiveTalk.COL_SPEAKER_ID}) REFERENCES ${SpeakerDaoSqlite.TABLE}(${SpeakerDaoSqlite.COL_ID}) ON DELETE CASCADE"
    ).execute(database)

  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

  }

  private inline fun selectAll() = SELECT(
      "$TABLE.$COL_ID AS $COL_ID",
      COL_TITLE,
      COL_DESCRIPTION,
      COL_START_TIME,
      COL_END_TIME,
      COL_TAGS,
      COL_FAVORITE,
      "${LocationDaoSqlite.TABLE}.${LocationDaoSqlite.COL_ID} AS $COL_LOCATION_ID",
      "${LocationDaoSqlite.TABLE}.${LocationDaoSqlite.COL_NAME} AS $COL_LOCATION_NAME",
      "${SpeakerDaoSqlite.TABLE}.${SpeakerDaoSqlite.COL_ID} AS $COL_SPEAKER_ID",
      "${SpeakerDaoSqlite.TABLE}.${SpeakerDaoSqlite.COL_NAME} AS $COL_SPEAKER_NAME",
      "${SpeakerDaoSqlite.TABLE}.${SpeakerDaoSqlite.COL_PICTURE} AS $COL_SPEAKER_PIC",
      "${SpeakerDaoSqlite.TABLE}.${SpeakerDaoSqlite.COL_COMPANY} AS $COL_SPEAKER_COMPANY",
      "${SpeakerDaoSqlite.TABLE}.${SpeakerDaoSqlite.COL_JOB_TITLE} AS $COL_SPEAKER_JOBTITLE"
  )
      .FROM(TABLE)
      .LEFT_OUTER_JOIN(LocationDaoSqlite.TABLE)
      .ON("$TABLE.$COL_LOCATION_ID = ${LocationDaoSqlite.TABLE}.${LocationDaoSqlite.COL_ID}")
      .LEFT_OUTER_JOIN(GiveTalk.TABLE)
      .ON("$TABLE.$COL_ID = ${GiveTalk.TABLE}.${GiveTalk.COL_SESSION_ID}")
      .LEFT_OUTER_JOIN(SpeakerDaoSqlite.TABLE)
      .ON("${GiveTalk.TABLE}.${GiveTalk.COL_SPEAKER_ID} = ${SpeakerDaoSqlite.TABLE}.${SpeakerDaoSqlite.COL_ID}")

  override fun getSessions(): Observable<List<Session>> = RxJavaInterop.toV2Observable(
      query(selectAll())
          .run()
          .mapToList(SessionJoinResult.mapper())
          .map(::mapJoinResultToSessions)
          .map { it.sortedWith(sortByStartDate) }
  )

  override fun insertOrUpdate(session: Session, favorite: Boolean): Observable<Long> {
    val cv = ContentValues()
    cv.put(COL_ID, session.id())
    cv.putOrNull(COL_TITLE, session.title())
    cv.putOrNull(COL_DESCRIPTION, session.description())
    cv.putOrNull(COL_TAGS, session.tags())
    cv.putOrNull(COL_LOCATION_ID, session.locationId())
    cv.putOrNull(COL_START_TIME, session.startTime())
    cv.putOrNull(COL_END_TIME, session.endTime())
    cv.put(COL_FAVORITE, favorite)
    return RxJavaInterop.toV2Observable(
        insert(TABLE, cv, SQLiteDatabase.CONFLICT_REPLACE)
            .flatMap { delete(GiveTalk.TABLE, "${GiveTalk.COL_SESSION_ID} = ?", session.id()) }
            .map {
              session.speakers().forEach {
                // Insert blocking
                val cv = ContentValues()
                cv.put(GiveTalk.COL_SESSION_ID, session.id())
                cv.put(GiveTalk.COL_SPEAKER_ID, it.id())
                getBriteDatabase().insert(GiveTalk.TABLE, cv)
              }
              1L // Everything was ok
            }
    )
  }

  override fun remove(id: String): Observable<Int> = RxJavaInterop.toV2Observable(
      delete(TABLE, "$COL_ID = ?",
          id).flatMap { deleted ->
        delete(GiveTalk.TABLE, "${GiveTalk.COL_SESSION_ID} = ?", id).map { deleted }
      }
  )

  override fun removeAll(): Observable<Int> = RxJavaInterop.toV2Observable(delete(TABLE))

  override fun setFavorite(sessionId: String, favorite: Boolean): Observable<Int> {
    val cv = ContentValues()
    cv.put(COL_FAVORITE, favorite)
    return RxJavaInterop.toV2Observable(update(TABLE, cv, "$COL_ID = ?", sessionId))
  }

  override fun getById(id: String): Observable<Session> =
      RxJavaInterop.toV2Observable(
      query(selectAll().WHERE("$TABLE.$COL_ID = ?"))
          .args(id)
          .run()
          .mapToList(SessionJoinResult.mapper())
          .map(::mapJoinResultToSessions)
          .map {
            when (it.size) {
              0 -> throw IllegalStateException("Could not find session with id = $id")
              1 -> it[0]
              else -> throw Exception("Expected a single result but got ${it.size}")
            }
          }
      )

  override fun getFavoriteSessions(): Observable<List<Session>> =
      RxJavaInterop.toV2Observable(
      query(selectAll().WHERE("$COL_FAVORITE = 1"))
          .run()
          .mapToList(SessionJoinResult.mapper())
          .map(::mapJoinResultToSessions)
          .map { it.sortedWith(sortByStartDate) }
      )

  // TODO test
  override fun getSessionsOfSpeaker(speakerId: String): Observable<List<Session>> =
      RxJavaInterop.toV2Observable(
      query(selectAll().WHERE("$COL_SPEAKER_ID = ?"))
          .args(speakerId)
          .run()
          .mapToList(SessionJoinResult.mapper())
          .map(::mapJoinResultToSessions)
          .map { it.sortedWith(sortByStartDate) }
      )

  // TODO test
  override fun findSessionsWith(query: String): Observable<List<Session>> {

    val split = query.split(" ")
    val whereClauseBuilder = StringBuilder()
    val args = Array<String>(split.size * 3, { "" })
    var argsIndex = 0

    split.forEachIndexed { i, searchTerm ->
      if (i > 0) {
        whereClauseBuilder.append(" OR ")
      }
      whereClauseBuilder.append("$COL_TITLE LIKE ? OR $COL_DESCRIPTION LIKE ? OR $COL_TAGS LIKE ?")
      args[argsIndex++] = "%$searchTerm%"
      args[argsIndex++] = "%$searchTerm%"
      args[argsIndex++] = "%$searchTerm%"
    }

    return RxJavaInterop.toV2Observable(
        query(selectAll().WHERE(whereClauseBuilder.toString()))
        .args(*args)
        .run()
        .mapToList(SessionJoinResult.mapper())
        .map(::mapJoinResultToSessions)
        .map { it.sortedWith(sortByStartDate) }
    )
  }

  override fun getBriteDatabase(): BriteDatabase = db
}