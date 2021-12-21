package com.kalex.sp_aplication.presentation.validations

import java.io.File

fun File.getFileSizeFloat(): Double {
    return this.length().toDouble().div(1024)
}