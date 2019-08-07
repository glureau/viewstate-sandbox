package com.glureau.poc.users

import com.glureau.poc.common.domain.User

data class UsersViewState(
    val users: List<UserViewState> = emptyList()
)

data class UserViewState(
    val firstName: String,
    val lastName: String,
    val age: String
)
fun User.toViewState() = UserViewState(firstName, lastName, age.toString())
