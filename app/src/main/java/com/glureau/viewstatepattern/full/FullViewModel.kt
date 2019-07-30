package com.glureau.viewstatepattern.full

import com.glureau.viewstatepattern.common.api.FakeRepository
import com.glureau.viewstatepattern.common.api.toDomain
import com.glureau.viewstatepattern.common.api.toDto
import com.glureau.viewstatepattern.common.domain.User
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class FullViewModel {
    companion object {
        private const val NAME_MIN = 2
        private const val NAME_MAX = 20
        private val NAME_AUTHORIZED_CHARS = Regex("[a-zA-Z]*")

        private const val AGE_MIN = 18
        private const val AGE_MAX = 120
    }

    private val repository = FakeRepository()

    fun registerNewUser(firstName: String, lastName: String, age: String) =
        repository.registerNewUser(User(firstName, lastName, age.toInt()).toDto())

    // TODO: Should react instantly to new user instead of reacting modulo 2s
    fun getAllUsers() =
        Observable.interval(0, 2, TimeUnit.SECONDS)
            .flatMap { repository.getAllUsers() }
            .map { set -> set.map { it.toDomain() } }

    fun validateFirstName(firstName: String) = validateName(firstName)
    fun validateLastName(lastName: String) = validateName(lastName)
    fun validateAge(ageStr: String): AgeValidation {
        return try {
            val age = ageStr.toInt()
            if (age in AGE_MIN..AGE_MAX) AgeValidation.OK else if (age < AGE_MIN) AgeValidation.TOO_YOUNG else AgeValidation.TOO_OLD
        } catch (nfe: NumberFormatException) {
            AgeValidation.AGE_ERROR
        }
    }

    private fun validateName(name: String): NameValidation {
        if (name.length !in NAME_MIN..NAME_MAX) return NameValidation.WRONG_LENGTH
        if (!NAME_AUTHORIZED_CHARS.matches(name)) return NameValidation.WRONG_CHARS
        return NameValidation.OK
    }

    enum class NameValidation(val errorMessage: String? = null) {
        OK,
        WRONG_LENGTH("Name length should be at least $NAME_MIN and at max $NAME_MAX"),
        WRONG_CHARS("Name contains bad characters")
    }

    enum class AgeValidation(val errorMessage: String? = null) {
        OK,
        TOO_YOUNG("You're too young!"),
        TOO_OLD("You're too old!"),
        AGE_ERROR("Error with your age, please check again"),
    }
}
