package com.kalex.sp_aplication.presentation.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun DocumentListItem(
    date: String,
    documentType: String,
    id: String,
    name: String,
    lastName: String,
    onItemClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(id) }
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column() {
            val newFecha = date.split("T")
            val vecFecha = newFecha[0].split("-")
            Text(
                text = "${vecFecha[2]}/${vecFecha[1]}/${vecFecha[0]} - $documentType ",
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(text = "$name " + "$lastName")
        }
        Icon(
            Icons.Filled.KeyboardArrowRight,
            "ver detalle documento",
        )
    }
    Divider(color = Color.Black, thickness = 1.dp)
}
