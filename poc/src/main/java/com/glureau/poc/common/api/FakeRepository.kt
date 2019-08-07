package com.glureau.poc.common.api

import dagger.Reusable
import io.reactivex.Completable
import io.reactivex.Observable
import java.io.IOException
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

@Reusable
class FakeRepository @Inject constructor() {

    private val knownUsers = mutableSetOf<UserDto>()

    init {
        knownUsers.add(UserDto("John", "Doe", 21))
        knownUsers.add(UserDto("Jane", "Dae", 21))
        knownUsers.add(UserDto("John", "Smith", 33))
    }

    fun registerNewUser(user: UserDto): Completable {
        if (knownUsers.contains(user)) {
            return Completable.error(IllegalStateException("User already exists"))
        }
        return when (Random.nextInt(0, 5)) {
            0, 1, 2 -> Completable.complete().delay(200, TimeUnit.MILLISECONDS).doOnComplete { knownUsers.add(user) }
            3 -> Completable.error(IOException("Cannot reach server")).delay(500, TimeUnit.MILLISECONDS)
            else -> Completable.error(CancellationException("Connection reset")).delay(2000, TimeUnit.MILLISECONDS)
        }
    }

    fun getAllUsers(): Observable<Set<UserDto>> {
        return when (Random.nextInt(0, 2)) {
            0, 1, 2 -> Observable.just(knownUsers.toSet()).delay(200, TimeUnit.MILLISECONDS)
            3 -> Observable.error<Set<UserDto>>(IOException("Cannot reach server")).delay(500, TimeUnit.MILLISECONDS)
            else -> Observable.error<Set<UserDto>>(CancellationException("Connection reset")).delay(
                2000,
                TimeUnit.MILLISECONDS
            )
        }
    }
}