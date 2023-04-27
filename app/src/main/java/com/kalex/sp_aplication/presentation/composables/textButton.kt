package com.kalex.sp_aplication.presentation.composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ButtonText(msg: String, fontSize: Int = 20) {
    Text(
        text = msg,
        fontSize = fontSize.sp,
    )
}
