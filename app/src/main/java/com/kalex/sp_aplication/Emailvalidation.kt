package com.kalex.sp_aplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.regex.Pattern

class Emailvalidation() {
    var text by mutableStateOf("")
    private val errorMessage : String = "Correo no valido "
    var error by mutableStateOf<String?>(null)

    fun validate(){
        error = if (isEmailValid(text)){
            null
        }else {
            errorMessage
        }
    }
    fun valid ():Boolean = isEmailValid(text);
}

private const val EMAIL_REGEX = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})\$"
private fun isEmailValid(email : String ):Boolean{
 return Pattern.matches(EMAIL_REGEX,email)
}