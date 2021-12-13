package com.kalex.sp_aplication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kalex.sp_aplication.common.Constants
import com.kalex.sp_aplication.presentation.ui.*
import com.kalex.sp_aplication.presentation.ui.verDocumentos

@Composable
fun Navegacion(){
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
            Home(navController ,nombre)

        }
        composable(Constants.SendDocNavItem){
            enviarDocumento(navController)
        }
        composable(Constants.getDocNavItem){
            verDocumentos(navController)
        }
        composable(Constants.oficesNavItem){
            VerOficinas(navController)
        }
    }
}