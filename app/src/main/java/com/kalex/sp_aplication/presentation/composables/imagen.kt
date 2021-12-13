package com.kalex.sp_aplication.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.kalex.sp_aplication.R

@Composable
fun Imagen(url:Any,modifier: Modifier) {
    Image(
        painter = rememberImagePainter(
            data = url,
            builder = { crossfade(true) }
        ),
        contentDescription = "Logo SP",
        modifier
    )
}