package de.droidcon.berlin2017.updater

import io.reactivex.Observable

/**
 *
 *
 * @author Hannes Dorfmann
 */
interface AppUpdateChecker {

  /**
   * Emits new item whenever a new version of
   */
  fun newAppVersionAvailable() : Observable<AppVersion>
}