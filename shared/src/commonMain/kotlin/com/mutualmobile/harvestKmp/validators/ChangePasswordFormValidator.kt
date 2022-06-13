package com.mutualmobile.harvestKmp.validators

import com.mutualmobile.harvestKmp.validators.exceptions.EmptyFieldException
import com.mutualmobile.harvestKmp.validators.exceptions.InvalidPasswordException
import com.mutualmobile.harvestKmp.validators.exceptions.SamePasswordException

class ChangePasswordFormValidator {

    operator fun invoke(password: String, oldPassword: String) {
        if (password.isEmpty()) {
            throw EmptyFieldException("Password")
        }
        if (oldPassword.isEmpty()) {
            throw EmptyFieldException("Confirm Password")
        }
        if (!isValidPassword(password)) {
            throw InvalidPasswordException()
        }
        // Commenting the code because some initial sign-up passwords might not match this criteria
//        if (!isValidPassword(oldPassword)) {
//            throw InvalidPasswordException()
//        }

        if (password == oldPassword) {
            throw SamePasswordException()
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