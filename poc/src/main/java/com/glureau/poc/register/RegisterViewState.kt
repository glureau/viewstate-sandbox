package com.glureau.poc.register

data class RegisterViewState(
    val firstName: String? = null,
    val firstNameError: String? = null,
    val lastName: String? = null,
    val lastNameError: String? = null,
    val age: String? = null,
    val ageError: String? = null,
    val submitError: String? = null
)
