package com.kalex.sp_aplication.presentation.ui

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kalex.sp_aplication.presentation.viewModels.OficeViewModel
import com.kalex.sp_aplication.presentation.viewModels.UserViewModel


@Composable
fun VerOficinas(navController: NavHostController,
                viewModel1 : OficeViewModel = hiltViewModel()
) {

    ToolBarOfice(navController)
}
@Composable
fun ToolBarOfice(
    navController: NavHostController
){
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Oficinas") },
            navigationIcon =
            {
                IconButton(onClick = { navController.popBackStack() },

                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "go back"
                    )
                }
            },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Menu,
                        contentDescription = "menu hamburgesa")
                }
            },
            backgroundColor = Color.White
        )
    }) {

    }
}