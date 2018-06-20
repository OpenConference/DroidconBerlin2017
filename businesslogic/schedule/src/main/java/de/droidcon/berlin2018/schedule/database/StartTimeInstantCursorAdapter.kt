package de.droidcon.berlin2018.schedule.database

import android.database.Cursor
import de.droidcon.berlin2018.schedule.database.dao.SessionDaoSqlite
import org.threeten.bp.Instant

/**
 * A auto-value-cursor CursorAdapter
 *
 * @author Hannes Dorfmann
 */
class StartTimeInstantCursorAdapter {

  companion object {
    @JvmStatic
    fun createFromCursor(c: Cursor): Instant? {
      val index = c.getColumnIndex(SessionDaoSqlite.COL_START_TIME)
      return if (index >= 0) {
        if (c.isNull(index)) {
          null
        } else {
          Instant.ofEpochMilli(c.getLong(index))
        }
      } else null
    }
  }
}
