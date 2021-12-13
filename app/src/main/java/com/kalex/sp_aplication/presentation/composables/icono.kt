package com.kalex.sp_aplication.presentation.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

@Composable
fun Icono(url : Any, valor: Int) {
    Icon(
        painter = rememberImagePainter(url),
        contentDescription = null,
        modifier = Modifier.size(valor.dp),
    )
}
@Composable
fun IconoVector(url : ImageVector, valor: Int) {
    Icon(
        imageVector = url ,
        contentDescription = null,
        modifier = Modifier.size(valor.dp),
    )
}