package com.kalex.sp_aplication.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.rememberImagePainter

@Composable
fun Image(url: Any?, modifier: Modifier) {
    Image(
        painter = rememberImagePainter(
            data = url,
            builder = { crossfade(true) },
        ),
        contentDescription = "Logo SP",
        modifier,
    )
}
