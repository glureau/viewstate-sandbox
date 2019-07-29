package com.glureau.viewstatepattern.naive_commented

import com.glureau.viewstatepattern.common.api.FakeRepository
import com.glureau.viewstatepattern.common.api.toDomain
import com.glureau.viewstatepattern.common.api.toDto
import com.glureau.viewstatepattern.common.domain.User
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class NaiveCommentedViewModel {

    private val repository = FakeRepository()

    //TODO: Implement check on data:
    // - 2 < Strings.length < 20
    // - 18 < age < 120
    fun registerNewUser(firstName: String, lastName: String, age: Int) =
        repository.registerNewUser(User(firstName, lastName, age).toDto())

    // TODO: Should react instantly to new user instead of reacting modulo 2s
    fun getAllUsers() =
        Observable.interval(0, 2, TimeUnit.SECONDS)
            .flatMap { repository.getAllUsers() }
            .map { set -> set.map { it.toDomain() } }
}
