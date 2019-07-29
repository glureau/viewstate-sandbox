package com.glureau.viewstatepattern.common.api

import com.glureau.viewstatepattern.domain.User

data class UserDto(val f: String, val l: String, val a: Int)

fun User.toDto() = UserDto(firstName, lastName, age)
fun UserDto.toDomain() = User(f, l, a)
