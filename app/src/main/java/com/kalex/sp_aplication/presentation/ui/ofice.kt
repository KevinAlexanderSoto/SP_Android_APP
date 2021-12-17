package com.kalex.sp_aplication.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kalex.sp_aplication.presentation.viewModels.OficeViewModel
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import com.kalex.sp_aplication.presentation.composables.Drawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun VerOficinas(navController: NavHostController,
                viewModel : OficeViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    )
    val scope = rememberCoroutineScope()

    val resp = viewModel.state.value

    if (resp.isLoading){
        Box(modifier = Modifier.fillMaxSize()
            , contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize(0.1f)
            )

        }
    }

    if (!resp.isLoading){
        ToolBarOfice(navController,scope,scaffoldState)
    }
}
@Composable
fun ToolBarOfice(
    navController: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
){
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
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
                IconButton(onClick = {
                    scope.launch {
                    scaffoldState.drawerState.open()
                }}) {
                    Icon(imageVector = Icons.Default.Menu,
                        contentDescription = "menu hamburgesa")
                }
            },
            backgroundColor = Color.White
        )
    },
        drawerContent = { Drawer(scope, scaffoldState, navController,) },
        drawerGesturesEnabled = true
        ) {

    }
}