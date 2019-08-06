package com.glureau.viewstatepattern.view_state

import com.glureau.viewstatepattern.common.api.FakeRepository
import com.glureau.viewstatepattern.common.api.toDomain
import com.glureau.viewstatepattern.common.api.toDto
import com.glureau.viewstatepattern.common.domain.User
import com.glureau.viewstatepattern.view_state.common.ViewStateProvider
import com.jakewharton.rxbinding2.InitialValueObservable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class ViewStateViewModel : ViewStateProvider<ViewState>(ViewState()) {

    companion object {
        private const val NAME_MIN = 2
        private const val NAME_MAX = 20
        private val NAME_AUTHORIZED_CHARS = Regex("[a-zA-Z]*")

        private const val AGE_MIN = 18
        private const val AGE_MAX = 120
    }

    private val repository = FakeRepository()

    fun onFirstNameChanged(firstNameObs: InitialValueObservable<CharSequence>) {
        firstNameObs.skipAndSubscribe { name ->
            currentViewState.copy(
                firstName = name.toString(),
                firstNameError = validateName(name.toString()).errorMessage
            ).emit()
        }
    }

    fun onLastNameChanged(lastNameObs: InitialValueObservable<CharSequence>) {
        lastNameObs.skipAndSubscribe { name ->
            currentViewState.copy(
                lastName = name.toString(),
                lastNameError = validateName(name.toString()).errorMessage
            ).emit()
        }
    }

    fun onAgeChanged(ageObs: InitialValueObservable<CharSequence>) {
        ageObs.skipAndSubscribe { ageStr ->
            currentViewState.copy(
                age = ageStr.toString(),
                ageError = validateAge(ageStr.toString()).errorMessage
            ).emit()
        }
    }

    fun onSubmit(clicks: Observable<Any>) {
        disposables.add(clicks.subscribe {
            if (validateName(currentViewState.firstName) == NameValidation.OK &&
                validateName(currentViewState.lastName) == NameValidation.OK &&
                validateAge(currentViewState.age) == AgeValidation.OK
            ) {
                registerNewUser()
                    .doOnSubscribe { currentViewState.copy(submitError = "").emit() }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        currentViewState.copy(firstName = "", lastName = "", age = "").emit()
                    }, {
                        currentViewState.copy(submitError = "Cannot register: ${it.message}").emit()
                    })
            } else {
                currentViewState.copy(submitError = "Verify your data before register").emit()
            }
        })
    }

    private fun registerNewUser() =
        repository.registerNewUser(
            User(
                currentViewState.firstName,
                currentViewState.lastName,
                currentViewState.age.toInt()
            ).toDto()
        )

    private var pollingAllUsers: Disposable? = null
    // TODO: Should react instantly to new user instead of reacting modulo 2s
    override fun onViewStateSubscribed() {
        disposables.add(Observable.interval(0, 2, TimeUnit.SECONDS)
            .flatMap { repository.getAllUsers() }
            .map { set -> set.map { it.toDomain() } }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { users ->
                currentViewState.copy(users = users.map { it.toViewState() }).emit()
            })
    }

    override fun onViewStateUnsubscribed() {
        pollingAllUsers?.dispose()
    }

    private fun validateAge(ageStr: String): AgeValidation {
        return try {
            if (ageStr.isEmpty()) return AgeValidation.EMPTY
            val age = ageStr.toInt()
            if (age in AGE_MIN..AGE_MAX) AgeValidation.OK else if (age < AGE_MIN) AgeValidation.TOO_YOUNG else AgeValidation.TOO_OLD
        } catch (nfe: NumberFormatException) {
            AgeValidation.AGE_ERROR
        }
    }

    private fun validateName(name: String): NameValidation {
        if (name.isEmpty()) return NameValidation.EMPTY // Ignore error
        if (name.length !in NAME_MIN..NAME_MAX) return NameValidation.WRONG_LENGTH
        if (!NAME_AUTHORIZED_CHARS.matches(name)) return NameValidation.WRONG_CHARS
        return NameValidation.OK
    }

    enum class NameValidation(val errorMessage: String? = null) {
        EMPTY,
        OK,
        WRONG_LENGTH("Name length should be at least $NAME_MIN and at max $NAME_MAX"),
        WRONG_CHARS("Name contains bad characters")
    }

    enum class AgeValidation(val errorMessage: String? = null) {
        EMPTY,
        OK,
        TOO_YOUNG("You're too young!"),
        TOO_OLD("You're too old!"),
        AGE_ERROR("Error with your age, please check again"),
    }
}
