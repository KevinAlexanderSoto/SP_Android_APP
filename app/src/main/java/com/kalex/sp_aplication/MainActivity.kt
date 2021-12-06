package com.kalex.sp_aplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.runtime.Composable

import androidx.compose.ui.tooling.preview.Preview
import com.kalex.sp_aplication.Model.SignInViewModelFactory
import com.kalex.sp_aplication.Model.SingInViewModel
import com.kalex.sp_aplication.ui.SingIn

import com.kalex.sp_aplication.theme.SPAplicationTheme



class MainActivity : ComponentActivity() {
    private val viewModel: SingInViewModel by viewModels{ SignInViewModelFactory()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SPAplicationTheme {
                SingIn {
                email : String, password :String ->
                viewModel.singIn(email, password)
            }
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SPAplicationTheme {
        SingIn {
                email : String, password :String ->
            viewModel.singIn(email, password)
        }
    }
}*/
