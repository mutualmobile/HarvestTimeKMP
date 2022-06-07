package com.mutualmobile.harvestKmp.validators

import com.mutualmobile.harvestKmp.validators.exceptions.EmptyFieldException
import com.mutualmobile.harvestKmp.validators.exceptions.InvalidEmailException

class ForgotPasswordFormValidator {

    operator fun invoke(email: String) {
        if (email.isEmpty()) {
            throw EmptyFieldException("Email")
        }
        if (!isValidEmail(email)) {
            throw InvalidEmailException()
        }
    }

    private fun isValidEmail(target: String): Boolean {
        return target.isNotEmpty() && EMAIL_ADDRESS_PATTERN.matches(target)
    }

    companion object {
        private val RFC_5322_REGEX =
            Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")

        val EMAIL_ADDRESS_PATTERN = RFC_5322_REGEX
    }
}