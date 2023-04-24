package com.kalex.sp_aplication.camara

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@ExperimentalPermissionsApi
@Composable
fun Permission(
    permission: String = android.Manifest.permission.CAMERA,
    rationale: String = "Esto es importante para el funcionamiento de la Aplicacion",
    permissionNotAvailableContent: @Composable () -> Unit = { },
    content: @Composable () -> Unit = { },
) {
    val permissionState = rememberPermissionState(permission)
    PermissionRequired(
        permissionState = permissionState,
        permissionNotGrantedContent = {
            Rationale(
                text = rationale,
                onRequestPermission = { permissionState.launchPermissionRequest() },
            )
        },
        permissionNotAvailableContent = permissionNotAvailableContent,
        content = content,
    )
}

@Composable
private fun Rationale(
    text: String,
    onRequestPermission: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = { /* Don't */ },
        title = {
            Text(text = "Requerimiento de Permisos")
        },
        text = {
            Text(text)
        },
        confirmButton = {
            Button(onClick = onRequestPermission) {
                Text("Ok")
            }
        },
    )
}
