package de.droidcon.berlin2017.ui.lce

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


/**
 * Just a shorthand method to create observables that are
 *
 * @author Hannes Dorfmann
 */

fun <T> lceObservable(dataSource: Observable<T>): Observable<LceViewState<T>> =
    dataSource
        .map { LceViewState.Content(it) as LceViewState<T> }
        .subscribeOn(Schedulers.io())
        .onErrorReturn {
          Timber.e(it)
          LceViewState.Error<T>(it)
        }
        .startWith(LceViewState.Loading<T>())
        .observeOn(AndroidSchedulers.mainThread())
