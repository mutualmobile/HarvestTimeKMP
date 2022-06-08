package com.mutualmobile.harvestKmp.validators

import com.mutualmobile.harvestKmp.validators.exceptions.EmptyFieldException
import com.mutualmobile.harvestKmp.validators.exceptions.InvalidPasswordException
import com.mutualmobile.harvestKmp.validators.exceptions.PasswordMismatchException

class ChangePasswordFormValidator {

    operator fun invoke(password: String, confirmPassword: String) {
        if (password.isEmpty()) {
            throw EmptyFieldException("First Name")
        }
        if (confirmPassword.isEmpty()) {
            throw EmptyFieldException("Last Name")
        }
        if (!isValidPassword(password)) {
            throw InvalidPasswordException()
        }
        if (!isValidPassword(confirmPassword)) {
            throw InvalidPasswordException()
        }

        if (!password.equals(confirmPassword)) {
            throw PasswordMismatchException()
        }
    }


    private fun isValidPassword(target: String): Boolean {
        return target.isNotEmpty() && PASSWORD_PATTERN.matches(target)
    }

    companion object {
        private val PASSWORD_REGEX =
            Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
        val PASSWORD_PATTERN = PASSWORD_REGEX
    }
}