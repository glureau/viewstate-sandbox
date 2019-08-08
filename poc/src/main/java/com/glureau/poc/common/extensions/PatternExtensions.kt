package com.glureau.poc.common.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.glureau.poc.common.pattern.ViewStateEffectsProvider
import com.glureau.poc.common.pattern.ViewStateProvider
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable

fun <T> Fragment.observeViewState(viewStateProvider: ViewStateProvider<T>, onState: (T) -> Unit) {
    viewStateProvider.viewState
        .autoDisposable(AndroidLifecycleScopeProvider.from(this))
        .subscribe(onState)
}

fun <T> ViewStateProvider<T>.observeViewState(
    lifecycleOwner: LifecycleOwner,
    onStateChanged: (T) -> Unit
) {
    viewState
        .autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
        .subscribe(onStateChanged)
}

fun <S, E> ViewStateEffectsProvider<S, E>.observeViewEffects(
    lifecycleOwner: LifecycleOwner,
    onEffectSent: (E) -> Unit
) {
    viewEffects
        .autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner))
        .subscribe(onEffectSent)
}