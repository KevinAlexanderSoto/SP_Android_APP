package com.kalex.sp_aplication.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.sp_aplication.common.Constants
import com.kalex.sp_aplication.presentation.ui.*

@RequiresApi(Build.VERSION_CODES.Q)
@ExperimentalPermissionsApi
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Constants.MainNavItem,
    ) {
        composable(Constants.MainNavItem) {
            SingIn(navController)
        }
        composable(
            Constants.HomeNavItem,
        ) {
                backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre")
            requireNotNull(nombre)
            Home(navController, nombre)
        }
        composable(Constants.SendDocNavItem) {
            EnviarDocumento(navController)
        }
        composable(Constants.getDocNavItem) {
            VerDocumentos(navController)
        }
        composable(Constants.getDocDetailNavItem) {
                backStackEntry ->
            val idRegistro = backStackEntry.arguments?.getString("idRegistro")
            requireNotNull(idRegistro)
            VerDocumento(navController, idRegistro = idRegistro)
        }
        composable(Constants.oficesNavItem) {
            VerOficinas(navController)
        }
    }
}
