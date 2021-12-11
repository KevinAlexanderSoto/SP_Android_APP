package com.kalex.sp_aplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kalex.sp_aplication.Model.SignInViewModelFactory
import com.kalex.sp_aplication.Model.SingInViewModel
import com.kalex.sp_aplication.ui.SingIn

import com.kalex.sp_aplication.theme.SPAplicationTheme
import com.kalex.sp_aplication.ui.Home
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val viewModel: SingInViewModel by viewModels{ SignInViewModelFactory() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SPAplicationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "singin" ){
                    composable("singin"){
                        SingIn {email : String, password :String ->
                            viewModel.singIn(email, password)
                            viewModel.respuesta
                        }
                    }
                    composable("home"){
                        Home()

                    }
                    composable("enviarDoc"){
                        Home()
                    }
                    composable("getDoc"){
                        Home()
                    }
                    composable("oficinas"){
                        Home()
                    }
                }
            }
        }
    }
}

