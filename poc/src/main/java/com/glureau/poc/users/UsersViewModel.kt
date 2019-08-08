package com.glureau.poc.users

import com.glureau.poc.R
import com.glureau.poc.common.api.FakeRepository
import com.glureau.poc.common.api.toDomain
import com.glureau.poc.common.pattern.StringResolver
import com.glureau.poc.common.pattern.ViewStateEffectsProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UsersViewModel @Inject constructor(
    private val repository: FakeRepository,
    private val stringResolver: StringResolver
) : ViewStateEffectsProvider<UsersViewState, UserViewEffects>(UsersViewState()) {

    init {
        // Stupid implementation, to show that even if repository triggers error, streams are safe
        addDisposable(Observable.interval(1, TimeUnit.SECONDS)
            .flatMap { repository.getAllUsers() }
            .map { set -> set.map { it.toDomain() } }
            .doOnError { error ->
                sendEffect(
                    when (error) {
                        is IOException -> ioErrorEffect()
                        else -> genericErrorEffect()
                    }
                )
            }
            .retry()
            .subscribe { users ->
                updateState { state -> state.copy(users = users.map { it.toViewState() }) }
            }
        )
    }

    private fun genericErrorEffect() =
        UserViewEffects.GenericErrorMessage(
            stringResolver.getString(R.string.generic_error),
            stringResolver.getString(R.string.retry)
        )

    private fun ioErrorEffect() =
        UserViewEffects.IoErrorMessage(
            stringResolver.getString(R.string.io_error),
            stringResolver.getString(R.string.retry)
        )
}
