package com.kalex.sp_aplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent


import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kalex.sp_aplication.presentation.navigation.Navegacion
import com.kalex.sp_aplication.presentation.ui.SingIn

import com.kalex.sp_aplication.presentation.theme.SPAplicationTheme
import com.kalex.sp_aplication.presentation.ui.Home
import com.kalex.sp_aplication.presentation.ui.enviarDocumento
import com.kalex.sp_aplication.presentation.ui.verDocumentos
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SPAplicationTheme {
                Navegacion()
            }
        }
    }
}

