package com.glureau.viewstatepattern.view_state.common

import com.jakewharton.rxbinding2.InitialValueObservable
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

open class ViewStateProvider<T>(var currentViewState: T) {
    private val relay = PublishRelay.create<T>()
    internal val disposables = CompositeDisposable()

    val viewState: Observable<T> = relay
        .doOnSubscribe { onViewStateSubscribed() }
        .doOnDispose {
            disposables.dispose()
            onViewStateUnsubscribed()
        }

    open fun onViewStateSubscribed() {}
    open fun onViewStateUnsubscribed() {}

    fun T.emit() {
        currentViewState = this
        relay.accept(currentViewState)
    }

    fun InitialValueObservable<CharSequence>.skipAndSubscribe(observer: (CharSequence) -> Unit) =
        disposables.add(this
            .skipInitialValue()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { observer(it) })
}