package com.kalex.usodecamara.galeria

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.sp_aplication.camara.Permission
import com.kalex.sp_aplication.presentation.ui.EMPTY_IMAGE_URI


@ExperimentalPermissionsApi
@Composable
fun GallerySelect(
    modifier: Modifier = Modifier,
    onImageUri: (Uri) -> Unit = { }
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            println("uri de la galeria $uri")
            onImageUri(uri ?: EMPTY_IMAGE_URI)
        }
    )


@Composable
fun LaunchGallery() {
    SideEffect {
        launcher.launch("image/*")
    }
}

    // pedir permiso si la version de android es mayor a 10
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    Permission(
        permission = Manifest.permission.ACCESS_MEDIA_LOCATION,
        rationale = "Si quieres seleccionar una foto , debo acceder a la galeria",
        permissionNotAvailableContent = {
            Column(modifier) {
                Text("O noes! No puedo acceder a tu Galeria")
                Spacer(modifier = Modifier.height(9.dp))
                Row {
                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            context.startActivity(
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.fromParts("package", context.packageName, null)
                                }
                            )
                        }
                    ) {
                        Text("Abrir Configuración")
                    }
                    // If they don't want to grant permissions, this button will result in going back
                    Button(
                        modifier = Modifier.padding(4.dp),
                        onClick = {
                            onImageUri(EMPTY_IMAGE_URI)
                        }
                    ) {
                        Text("Usar la camara")
                    }
                }
            }
        },
    ) {
        LaunchGallery()
    }
} else {
    LaunchGallery()
}
}