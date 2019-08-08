package com.glureau.poc.register

import com.glureau.poc.common.api.FakeRepository
import com.glureau.poc.common.api.toDto
import com.glureau.poc.common.domain.User
import com.glureau.poc.common.pattern.ViewStateProvider
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val repository: FakeRepository
) : ViewStateProvider<RegisterViewState>(RegisterViewState()) {

    companion object {
        private const val NAME_MIN = 2
        private const val NAME_MAX = 20
        private val NAME_AUTHORIZED_CHARS = Regex("[a-zA-Z]*")

        private const val AGE_MIN = 18
        private const val AGE_MAX = 120
    }

    fun onFirstNameChanged(firstNameObs: InitialValueObservable<CharSequence>) {
        firstNameObs.skipAndObserves { name ->
            updateState {
                it.copy(
                    firstName = name.toString(),
                    firstNameError = validateName(name.toString()).errorMessage
                )
            }
        }
    }

    fun onLastNameChanged(lastNameObs: InitialValueObservable<CharSequence>) {
        lastNameObs.skipAndObserves { name ->
            updateState {
                it.copy(
                    lastName = name.toString(),
                    lastNameError = validateName(name.toString()).errorMessage
                )
            }
        }
    }

    fun onAgeChanged(ageObs: InitialValueObservable<CharSequence>) {
        ageObs.skipAndObserves { ageStr ->
            updateState {
                it.copy(
                    age = ageStr.toString(),
                    ageError = validateAge(ageStr.toString()).errorMessage
                )
            }
        }
    }

    fun onSubmit(clicks: Observable<Any>) {
        addDisposable(clicks.subscribe {
            if (validateName(currentViewState.firstName) == NameValidation.OK &&
                validateName(currentViewState.lastName) == NameValidation.OK &&
                validateAge(currentViewState.age) == AgeValidation.OK
            ) {
                registerNewUser()
                    .doOnSubscribe { updateState { it.copy(submitError = "") } }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        updateState { it.copy(firstName = "", lastName = "", age = "") }
                    }, { t ->
                        updateState { it.copy(submitError = "Cannot register: ${t.message}") }
                    })
            } else {
                updateState { it.copy(submitError = "Verify your data before register") }
            }
        })
    }

    private fun registerNewUser() =
        //TODO : rework nullability
        repository.registerNewUser(
            User(
                currentViewState.firstName!!,
                currentViewState.lastName!!,
                currentViewState.age!!.toInt()
            ).toDto()
        )

    private fun validateAge(ageStr: String?): AgeValidation {
        return try {
            if (ageStr.isNullOrEmpty()) return AgeValidation.EMPTY
            val age = ageStr.toInt()
            if (age in AGE_MIN..AGE_MAX) AgeValidation.OK else if (age < AGE_MIN) AgeValidation.TOO_YOUNG else AgeValidation.TOO_OLD
        } catch (nfe: NumberFormatException) {
            AgeValidation.AGE_ERROR
        }
    }

    private fun validateName(name: String?): NameValidation {
        if (name.isNullOrEmpty()) return NameValidation.EMPTY // Ignore error
        if (name.length !in NAME_MIN..NAME_MAX) return NameValidation.WRONG_LENGTH
        if (!NAME_AUTHORIZED_CHARS.matches(name)) return NameValidation.WRONG_CHARS
        return NameValidation.OK
    }

    private enum class NameValidation(val errorMessage: String? = null) {
        EMPTY,
        OK,
        WRONG_LENGTH("Name length should be at least $NAME_MIN and at max $NAME_MAX"),
        WRONG_CHARS("Name contains bad characters")
    }

    private enum class AgeValidation(val errorMessage: String? = null) {
        EMPTY,
        OK,
        TOO_YOUNG("You're too young!"),
        TOO_OLD("You're too old!"),
        AGE_ERROR("Error with your age, please check again"),
    }
}
