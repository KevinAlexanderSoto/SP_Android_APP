package com.kalex.sp_aplication.presentation.validations

fun validarString(dato:String):Boolean{
    if (dato.length >= 2){
        return true
    }else{
        return false
    }
}