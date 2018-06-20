package de.droidcon.berlin2018.schedule.database.dao

import com.squareup.sqlbrite.BriteDatabase
import de.droidcon.berlin2018.model.Session
import io.reactivex.Observable

/**
 * Data-Access-Object for [com.droidcon.model.Session]
 *
 * @author Hannes Dorfmann
 */
interface SessionDao {

  /**
   * Get the list with all sessions
   */
  fun getSessions(): Observable<List<Session>>

  /**
   * Get a single session by his id
   */
  fun getById(id: String): Observable<Session>

  /**
   * Insert or update a Session
   */
  fun insertOrUpdate(session: Session, favorite: Boolean): Observable<Long>

  /**
   * Removes a session
   * @return Observable with the number of deleted Items (should be 1 or 0)
   */
  fun remove(id: String): Observable<Int>

  /**
   * Removes all sessions
   * @return Observable with the number of deleted sessions
   */
  fun removeAll(): Observable<Int>

  /**
   * See [Session.favorite]
   */
  fun setFavorite(sessionId: String, favorite: Boolean): Observable<Int>

  /**
   * Get all sessions given by a speaker
   */
  fun getSessionsOfSpeaker(speakerId: String): Observable<List<Session>>

  /**
   * Get a list with all sessions marked as favorite
   */
  fun getFavoriteSessions(): Observable<List<Session>>

  /**
   * Find sessions containing the specified query
   */
  fun findSessionsWith(query: String): Observable<List<Session>>

  // TODO refactor that
  fun getBriteDatabase(): BriteDatabase

}
