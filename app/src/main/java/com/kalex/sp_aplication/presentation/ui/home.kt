package com.kalex.sp_aplication.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kalex.sp_aplication.R
import com.kalex.sp_aplication.common.Constants
import com.kalex.sp_aplication.presentation.composables.*
import com.kalex.sp_aplication.presentation.theme.blanco
import com.kalex.sp_aplication.presentation.theme.color1
import com.kalex.sp_aplication.presentation.theme.color2
import com.kalex.sp_aplication.presentation.theme.color3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable fun Home(
    navController: NavController,
    nombre: String,
) {
    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    )
    val scope = rememberCoroutineScope()

    ToolBar(nombre = nombre, navController, scope, scaffoldState)
}

@Composable
fun ToolBar(
    nombre: String,
    navController: NavController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = nombre) },
                actions = {
                    IconButton(onClick = {
                        scope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "menu hamburgesa",
                        )
                    }
                },
            )
        },
        drawerContent = { Drawer(scope, scaffoldState, navController) },
        drawerGesturesEnabled = true,

    ) {
            padding ->
        Contenido(navController)
    }
}

@Composable
fun Contenido(
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // para hacer scroll
        verticalArrangement = Arrangement.spacedBy(19.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Imagen(
            url = R.drawable.people2,
            modifier = Modifier
                .wrapContentSize(Alignment.BottomCenter)
                .height(246.dp)
                .width(440.dp)
                .padding(2.dp),
        )
        Card(
            encabezado = "Enviar Documentos",
            R.drawable.upload_file_24,
            navController,
            Constants.SendDocNavItem,
            color1,

        )
        Card(
            encabezado = "Ver Documentos",
            R.drawable.plagiarism_24,
            navController,
            Constants.getDocNavItem,
            color2,
        )
        Card(
            encabezado = "Oficinas",
            R.drawable.location_on_24,
            navController,
            Constants.oficesNavItem,
            color3,
        )
        Spacer(modifier = Modifier.padding(3.dp))
    }
}

@Composable
fun Card(
    encabezado: String,
    urlIcono: Any,
    navController: NavController,
    route: String,
    colorItem: Color,
) {
    Card(
        elevation = 8.dp,
        // border = BorderStroke(0.5.dp, Color.Black),
        shape = RoundedCornerShape(17.dp),
        modifier = Modifier
            .fillMaxWidth(0.9f),

    ) {
        Column(
            modifier = Modifier.padding(15.dp)
                .fillMaxWidth(),
        ) {
            Row(modifier = Modifier.padding(2.dp, 1.dp, 5.dp, 20.dp)) {
                Icono(urlIcono, 30)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = encabezado, color = colorItem)
            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Button(
                    onClick = { navController.navigate(route) },
                    shape = RoundedCornerShape(24.dp),
                    contentPadding = PaddingValues(15.dp, 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorItem,
                        contentColor = blanco,
                    ),
                ) {
                    ButtonText("Ingresar", 15)
                    IconoVector(Icons.Default.ArrowForward, 20)
                }
            }
        }
    }
}
