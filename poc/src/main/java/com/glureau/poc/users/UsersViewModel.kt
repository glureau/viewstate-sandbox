package com.glureau.poc.users

import com.glureau.poc.common.api.FakeRepository
import com.glureau.poc.common.api.toDomain
import com.glureau.poc.common.api.toDto
import com.glureau.poc.common.domain.User
import com.glureau.poc.common.pattern.ViewStateProvider
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UsersViewModel @Inject constructor(
    private val repository: FakeRepository
) : ViewStateProvider<UsersViewState>(UsersViewState()) {

    // TODO: Should react instantly to new user instead of reacting modulo 2s -> Update Repository
    override fun onViewStateSubscribed() {
        addDisposable(Observable.interval(0, 2, TimeUnit.SECONDS)
            .flatMap { repository.getAllUsers() }
            .map { set -> set.map { it.toDomain() } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { users ->
                updateState { state -> state.copy(users = users.map { it.toViewState() }) }
            })
    }
}
