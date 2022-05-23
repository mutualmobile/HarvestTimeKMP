package com.mutualmobile.harvestKmp.validators

import com.mutualmobile.harvestKmp.validators.exceptions.EmptyFieldException
import com.mutualmobile.harvestKmp.validators.exceptions.InvalidEmailException

class LoginFormValidator  {

    fun invoke(email: String, password: String) {
        if (!isValidEmail(email)) {
            throw InvalidEmailException()
        }
        if (password.isEmpty()) {
            throw EmptyFieldException()
        }
    }

    private fun isValidEmail(target: String): Boolean {
        return target.isNotEmpty() && EMAIL_ADDRESS_PATTERN.matches(target)
    }

    companion object {
        val EMAIL_ADDRESS_PATTERN = Regex(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
    }
}