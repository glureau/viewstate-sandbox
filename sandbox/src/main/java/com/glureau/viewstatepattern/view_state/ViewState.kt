package com.glureau.viewstatepattern.view_state

import com.glureau.viewstatepattern.common.domain.User

data class ViewState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val age: String = "",
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
