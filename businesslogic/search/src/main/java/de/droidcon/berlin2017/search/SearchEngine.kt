package de.droidcon.berlin2017.search

import io.reactivex.Observable

/**
 *
 *
 * @author Hannes Dorfmann
 */
interface SearchEngine {

  fun search(query: String): Observable<List<SearchableItem>>
}