package com.kalex.sp_aplication.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.kalex.sp_aplication.common.Constants
import com.kalex.sp_aplication.data.remote.dto.ItemDocDto
import com.kalex.sp_aplication.domain.model.Item
import com.kalex.sp_aplication.presentation.composables.DocumentListItem
import com.kalex.sp_aplication.presentation.composables.Drawer
import com.kalex.sp_aplication.presentation.viewModels.DataViewModel
import com.kalex.sp_aplication.presentation.viewModels.DocumentViewModel
import com.kalex.sp_aplication.presentation.viewModels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun VerDocumentos(navController: NavHostController,
viewModel: DocumentViewModel = hiltViewModel(),
    viewmodelData : DataViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    )
    val scope = rememberCoroutineScope()
    //get documentos

   /* var correo = viewmodelData.correo
    viewModel.getDocuments(correo)*/

    var resp = viewModel.state.value
    //barra de cargando
    if (resp.isLoading){

        Box(modifier = Modifier.fillMaxSize()
            , contentAlignment = Alignment.Center
        ){
            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize(0.1f)

            )
        }
    }

    if (!resp.isLoading) {
        var docs = resp.document?.Items
        println("Respuesta server : $docs")
        ToolBarVerDocs(navController,docs,scope,scaffoldState)

    }

    if(resp.error.isNotBlank()) {
        Text(
            text = resp.error,
            color = MaterialTheme.colors.error,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)

        )
    }

}


@Composable
fun ToolBarVerDocs(
    navController: NavHostController,
    docs: List<ItemDocDto>?,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
    ) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
        TopAppBar(

            title = { Text(text = "Documentos") },
            navigationIcon =
            {
                IconButton(
                    onClick = { navController.popBackStack() },
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
                } }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "menu hamburgesa"
                    )
                }
            },

        )
    },
        drawerContent = { Drawer(scope, scaffoldState, navController,) },
        drawerGesturesEnabled = true
        ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
               docs?.forEach { it
                   item { DocumentListItem(
                       fecha = it.Fecha,
                       TipoAdjunto = it.TipoAdjunto,
                       idregistro = it.IdRegistro,
                       nombre = it.Nombre,
                       apellido = it.Apellido,
                       onItemClick = { it->
                           val idregistro = it
                           navController.navigate("getdocdetail/${idregistro}")
                       }
                   ) }
               }

            }

        }
    }
}
