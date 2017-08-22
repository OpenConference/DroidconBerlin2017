package de.droidcon.berlin2017.schedule.database.dao


import android.database.sqlite.SQLiteDatabase
import com.hannesdorfmann.sqlbrite.dao.Dao
import de.droidcon.berlin2017.model.Location
import de.droidcon.berlin2017.schedule.database.LocationAutoValue
import hu.akarnokd.rxjava.interop.RxJavaInterop
import io.reactivex.Observable

/**
 * DAO implementation that uses SqlBrite
 *
 * @author Hannes Dorfmann
 */
open class LocationDaoSqlite : LocationDao, Dao() {

  companion object {
    const val TABLE = "Location"
    const val COL_ID = "id"
    const val COL_NAME = "name"
  }


  override fun createTable(database: SQLiteDatabase) {
    CREATE_TABLE(TABLE,
        "$COL_ID VARCHAR(20) PRIMARY KEY",
        "$COL_NAME TEXT NOT NULL")
        .execute(database)

  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

  }

  override fun getLocations(): Observable<List<Location>> = RxJavaInterop.toV2Observable(
      query(
          SELECT(COL_ID, COL_NAME).FROM(TABLE)
      ).run()
          .mapToList(LocationAutoValue.mapper())
          .map { it as List<Location> }
  )

  override fun getById(id: String): Observable<Location> {
    TODO("Implement get location")
  }

  override fun insertOrUpdate(id: String, name: String): Observable<Long>
      = RxJavaInterop.toV2Observable(
      insert(TABLE, LocationAutoValue.create(id, name).toContentValues(),
          SQLiteDatabase.CONFLICT_REPLACE)
  )

  override fun remove(id: String): Observable<Int> = RxJavaInterop.toV2Observable(
      delete(TABLE, "$COL_ID = ?", id)
  )

  override fun removeAll(): Observable<Int> = RxJavaInterop.toV2Observable(
      delete(TABLE)
  )
}