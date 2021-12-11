package com.kalex.sp_aplication.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ButtonText(msg:String ,tamañoLetra:Int) {
    Text(
        text = msg,
        fontSize = tamañoLetra.sp,

        )
}