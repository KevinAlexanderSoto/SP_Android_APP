package com.kalex.sp_aplication.presentation.validations

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.regex.Pattern

class Emailvalidation() {
    var correo by mutableStateOf("")
    private val errorMessage: String = "Correo no valido "
    var error by mutableStateOf<String?>(null)

    fun validate() {
        error = if (isEmailValid(correo)) {
            null
        } else {
            errorMessage
        }
    }
    fun valid(): Boolean = isEmailValid(correo)
}

private const val EMAIL_REGEX = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})\$"
fun isEmailValid(email: String): Boolean {
    return Pattern.matches(EMAIL_REGEX, email)
}
