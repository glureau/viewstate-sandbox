package com.glureau.poc.common.pattern

import androidx.annotation.UiThread
import androidx.lifecycle.ViewModel
import com.jakewharton.rxbinding2.InitialValueObservable
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class ViewStateProvider<S>(var currentViewState: S) : ViewModel() {
    private val viewStateRelay = BehaviorRelay.createDefault(currentViewState)
    private val disposables = CompositeDisposable()
    val viewState = viewStateRelay.observeOn(AndroidSchedulers.mainThread())

    init {
        viewStateRelay.accept(currentViewState)
    }

    @UiThread
    protected fun updateState(stateUpdate: (S) -> S) {
        currentViewState = stateUpdate(currentViewState)
        viewStateRelay.accept(currentViewState)
    }

    protected fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }

    protected fun InitialValueObservable<CharSequence>.skipAndObserves(observer: (CharSequence) -> Unit) {
        disposables.add(this
            .skipInitialValue()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { observer(it) })
    }
}