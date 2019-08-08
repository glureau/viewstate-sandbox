package com.glureau.poc.users

import android.graphics.Color
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


sealed class UserViewEffects(open val message: String, open val action: String, val color: Int) {
    
    data class IoErrorMessage(override val message: String, override val action: String) :
        UserViewEffects(message, action, Color.BLUE)

    data class GenericErrorMessage(override val message: String, override val action: String) :
        UserViewEffects(message, action, Color.RED)
}
