package de.droidcon.berlin2017.schedule.database.dao

import android.database.sqlite.SQLiteDatabase
import com.hannesdorfmann.sqlbrite.dao.Dao
import de.droidcon.berlin2017.model.Speaker
import de.droidcon.berlin2017.schedule.database.SpeakerAutoValue
import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Observable


/**
 * A SpeakerDao on top of sqlite
 *
 * @author Hannes Dorfmann
 */
open class SpeakerDaoSqlite : SpeakerDao, Dao() {

  companion object {
    const val TABLE = "Speaker"
    const val COL_ID = "id"
    const val COL_NAME = "name"
    const val COL_INFO = "bio"
    const val COL_PICTURE = "picture"
    const val COL_COMPANY = "companyName"
    const val COL_JOB_TITLE = "jobTitle"
    const val COL_LINK1 = "link1"
    const val COL_LINK2 = "link2"
    const val COL_LINK3 = "link3"
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
  }

  override fun createTable(database: SQLiteDatabase) {

    CREATE_TABLE(TABLE,
        "$COL_ID VARCHAR(20) PRIMARY KEY",
        "$COL_NAME VARCHAR(100) NOT NULL",
        "$COL_INFO TEXT",
        "$COL_PICTURE TEXT",
        "$COL_COMPANY VARCHAR(100)",
        "$COL_JOB_TITLE TEXT",
        "$COL_LINK1 TEXT",
        "$COL_LINK2 TEXT",
        "$COL_LINK3 TEXT")
        .execute(database)

  }

  override fun insertOrUpdate(id: String, name: String, info: String?, profilePicUrl: String?,
      company: String?, jobTitle: String?, link1: String?, link2: String?,
      link3: String?): Observable<Long> = RxJavaInterop.toV2Observable(
      insert(TABLE,
          SpeakerAutoValue.create(id, name, info, profilePicUrl, company, jobTitle, link1, link2,
              link3).toContentValues(), SQLiteDatabase.CONFLICT_REPLACE)
  )

  override fun remove(id: String): Observable<Int> = RxJavaInterop.toV2Observable(
      delete(TABLE, "$COL_ID = ?", id)
  )

  override fun removeAll(): Observable<Int> = RxJavaInterop.toV2Observable(
      delete(TABLE)
  )

  override fun getSpeaker(id: String): Observable<Speaker> = RxJavaInterop.toV2Observable(
      query(
          SELECT("*").FROM(TABLE).WHERE("$COL_ID = ?")).args(id).run().mapToOneOrDefault(
          SpeakerAutoValue.mapper(),
          null).map { if (it == null) throw IllegalStateException() else it as Speaker }
  )

  override fun getSpeakers(): Observable<List<Speaker>> = RxJavaInterop.toV2Observable(
      query(
          SELECT("*").FROM(TABLE).ORDER_BY(COL_NAME)).run().mapToList(
          SpeakerAutoValue.mapper()).map { it as List<Speaker> }
  )

  override fun findSessionsWith(query: String): Observable<List<Speaker>> {

    val split = query.split(" ")
    val whereClauseBuilder = StringBuilder()
    val args = Array<String>(split.size * 3, { "" })
    var argsIndex = 0

    split.forEachIndexed { i, searchTerm ->
      if (i > 0) {
        whereClauseBuilder.append(" OR ")
      }
      whereClauseBuilder.append("${COL_NAME} LIKE ? OR ${COL_INFO} LIKE ? OR ${COL_COMPANY} LIKE ?")
      args[argsIndex++] = "%$searchTerm%"
      args[argsIndex++] = "%$searchTerm%"
      args[argsIndex++] = "%$searchTerm%"
    }

    return RxJavaInterop.toV2Observable(
        query(
            SELECT("*").FROM(TABLE).WHERE(whereClauseBuilder.toString()).ORDER_BY(
                COL_NAME))
            .args(*args)
            .run().mapToList(SpeakerAutoValue.mapper())
            .map { it as List<Speaker> }
    )
  }
}