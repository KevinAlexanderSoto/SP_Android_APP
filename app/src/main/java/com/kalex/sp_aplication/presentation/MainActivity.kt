package com.kalex.sp_aplication.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.SavedStateHandle

import com.kalex.sp_aplication.presentation.navigation.Navegacion

import com.kalex.sp_aplication.presentation.theme.SPAplicationTheme

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity () {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SPAplicationTheme {
                Navegacion()
            }
        }
    }
}

