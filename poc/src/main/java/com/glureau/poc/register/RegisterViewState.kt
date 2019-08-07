package com.glureau.poc.register

import com.glureau.poc.common.domain.User

data class RegisterViewState(
    val firstName: String? = null,
    val firstNameError: String? = null,
    val lastName: String? = null,
    val lastNameError: String? = null,
    val age: String? = null,
    val ageError: String? = null,
    val submitError: String? = null,
    val users: List<UserViewState> = emptyList()
)

data class UserViewState(
    val firstName: String,
    val lastName: String,
    val age: String
)
fun User.toViewState() = UserViewState(firstName, lastName, age.toString())
