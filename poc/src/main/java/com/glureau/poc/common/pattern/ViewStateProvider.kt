package com.glureau.poc.common.pattern

import androidx.lifecycle.ViewModel
import com.jakewharton.rxbinding2.InitialValueObservable
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

open class ViewStateProvider<T>(var currentViewState: T) : ViewModel() {
    private val relay = BehaviorRelay.create<T>()
    internal val disposables = CompositeDisposable()

    val viewState: Observable<T> = relay
        .doOnSubscribe { onViewStateSubscribed() }
        .doOnDispose {
            disposables.dispose()
            onViewStateUnsubscribed()
        }

    protected open fun onViewStateSubscribed() {}
    protected open fun onViewStateUnsubscribed() {}

    protected fun updateState(stateUpdate: (T) -> T) {
        currentViewState = stateUpdate(currentViewState)
        relay.accept(currentViewState)
    }

    protected fun InitialValueObservable<CharSequence>.skipAndSubscribe(observer: (CharSequence) -> Unit) =
        disposables.add(this
            .skipInitialValue()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { observer(it) })
}