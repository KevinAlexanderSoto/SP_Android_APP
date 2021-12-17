package com.kalex.sp_aplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kalex.sp_aplication.common.Constants
import com.kalex.sp_aplication.presentation.ui.*

@Composable
fun Navegacion() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Constants.MainNavItem)
    {
        composable(Constants.MainNavItem){
            SingIn(navController)
        }
        composable(Constants.HomeNavItem){
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
            val nombre= backStackEntry.arguments?.getString("idregistro")
            requireNotNull(nombre)
            VerDocumento(navController, idRegistro = nombre)
        }
        composable(Constants.oficesNavItem){
            VerOficinas(navController)
        }
    }
}