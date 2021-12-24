package com.kalex.sp_aplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.sp_aplication.common.Constants
import com.kalex.sp_aplication.presentation.ui.*
import java.io.File

/*TODO ESTO POR LA AUTENTICACION CON HUELLA :V , SE QUEDA ASI HASTA QUE SEPARA COMO MEJORARLO
@ExperimentalPermissionsApi
@Composable
fun Navegacion(onfiger: () -> Unit) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Constants.MainNavItem)
    {
        composable(Constants.MainNavItem){
            SingIn(navController){onfiger()}
        }
        composable(Constants.HomeNavItem
        ){
          backStackEntry->
               val nombre= backStackEntry.arguments?.getString("nombre")
            requireNotNull(nombre)
            Home(navController,nombre)

        }
        composable(Constants.SendDocNavItem){
            EnviarDocumento(navController)
        }
        composable(Constants.getDocNavItem){
            VerDocumentos(navController)
        }
        composable(Constants.getDocDetailNavItem){
                backStackEntry->
            val idRegistro= backStackEntry.arguments?.getString("idRegistro")
            requireNotNull(idRegistro)
            VerDocumento(navController, idRegistro = idRegistro)
        }
        composable(Constants.oficesNavItem){
            VerOficinas(navController)
        }
    }
}*/
