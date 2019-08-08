package com.glureau.poc.common.pattern

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

open class ViewStateEffectsProvider<S, E>(currentViewState: S) : ViewStateProvider<S>(currentViewState) {
    private val viewEffectsRelay = PublishRelay.create<E>()
    val viewEffects: Observable<E> = viewEffectsRelay.observeOn(AndroidSchedulers.mainThread())

    protected fun sendEffect(effect: E) {
        viewEffectsRelay.accept(effect)
    }
}