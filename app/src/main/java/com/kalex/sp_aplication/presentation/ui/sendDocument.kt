package com.kalex.sp_aplication.presentation.ui


import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController


@Composable
fun enviarDocumento(
    navController: NavHostController
) {
    ToolBar(navController)
}
@Composable
fun ToolBar(
    navController :NavHostController
){
    Scaffold(topBar = {
        TopAppBar(
            title = {  },
            navigationIcon =
                    {
                IconButton(onClick = {
                    navController.popBackStack()
                }
                //modifier = Modifier.padding(horizontal = 20.dp)
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