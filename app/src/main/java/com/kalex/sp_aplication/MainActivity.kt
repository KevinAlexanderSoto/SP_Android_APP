package com.kalex.sp_aplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.sp_aplication.presentation.navigation.Navigation
import com.kalex.sp_aplication.presentation.theme.SPAplicationTheme
import com.kalex.sp_aplication.presentation.ui.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalPermissionsApi
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SPAplicationTheme {
                Navigation()
            }
        }
    }
}
