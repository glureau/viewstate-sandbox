package com.glureau.viewstatepattern.naive

import com.glureau.viewstatepattern.common.api.FakeRepository
import com.glureau.viewstatepattern.common.api.toDomain
import com.glureau.viewstatepattern.common.api.toDto
import com.glureau.viewstatepattern.common.domain.User
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class NaiveViewModel {

    private val repository = FakeRepository()

    fun registerNewUser(firstName: String, lastName: String, age: Int) =
        repository.registerNewUser(User(firstName, lastName, age).toDto())

    fun getAllUsers() =
        Observable.interval(0, 2, TimeUnit.SECONDS)
            .flatMap { repository.getAllUsers() }
            .map { set -> set.map { it.toDomain() } }
}
