package com.kalex.sp_aplication.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kalex.sp_aplication.R
import com.kalex.sp_aplication.common.Constants
import com.kalex.sp_aplication.presentation.viewModels.DataViewModel
import com.kalex.sp_aplication.presentation.viewModels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Drawer(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController,
    viewModel : DataViewModel = hiltViewModel(),

) {
    //obtener nombre , para que se vuelva a pintar
    viewModel.settingsPrefs


    print("correo dataviewmodel : " +viewModel.correo )
    print("correo dataviewmodel : " +viewModel.constraseña )


    var nombre = viewModel.nombre

    Column {
        Image(
            painter = painterResource(id = R.drawable.hacemosquepase),
            contentDescription = "Bg Image",
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(15.dp))

            DrawerItem(texto = "Menú principal", R.drawable.home_24) {
                navController.navigate("home/$nombre") {
                    launchSingleTop = true
                }
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        DrawerItem(texto = "Enviar Documentos",R.drawable.upload_file_24) {

            navController.navigate(Constants.SendDocNavItem) {
                launchSingleTop = true
            }

            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
        DrawerItem(texto = "Ver Documentos", R.drawable.plagiarism_24) {
            navController.navigate(Constants.getDocNavItem) {
                launchSingleTop = true
            }

            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
        DrawerItem(texto = "Oficinas",R.drawable.location_on_24) {

            navController.navigate(Constants.oficesNavItem) {
                launchSingleTop = true
            }

            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
        DrawerItem(texto = "Cerrar sección",R.drawable.logout_20) {
            navController.navigate(Constants.MainNavItem) {
                launchSingleTop = true
            }

            scope.launch {
                scaffoldState.drawerState.close()
            }
        }
    }
}

@Composable
fun DrawerItem(
    texto:String,
    imagen :Any,
    onItemClick: ()->Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(6.dp)
            .clip(RoundedCornerShape(12))
            .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icono(imagen, 20)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text= texto,
            style = TextStyle(fontSize = 18.sp),

        )
    }
}