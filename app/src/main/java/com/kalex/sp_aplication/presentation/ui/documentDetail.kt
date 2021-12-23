package com.kalex.sp_aplication.presentation.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.asImageBitmap

import com.kalex.sp_aplication.presentation.composables.Drawer
import com.kalex.sp_aplication.presentation.viewModels.DocumentDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@Composable
fun VerDocumento(
    navController: NavHostController ,
    viewModel: DocumentDetailViewModel = hiltViewModel(),
    idRegistro :String
    ) {

    //para menu desplegable
    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    )
    val scope = rememberCoroutineScope()

    //get documentos
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

    //Termino de Cargar
    if (!resp.isLoading) {

        var doc :String = ""

        resp.document?.Items?.forEach { item->
            doc = item.Adjunto
        }
        // preparar img para decodificarlar

        val urlImg = doc.split(",")

        val decodeBitmap = urlImg[1].toBitmap()

        val imgBit = decodeBitmap.asImageBitmap()

        ToolBarVerDocsDetail(navController,scope,scaffoldState,imgBit)

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

fun String.toBitmap():Bitmap{
    Base64.decode(this,Base64.DEFAULT).apply {
        return BitmapFactory.decodeByteArray(this,0,size)
    }
}


@Composable
fun ToolBarVerDocsDetail(
    navController: NavHostController,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    imgBit: ImageBitmap,
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

        Column(modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(0.dp, 4.dp)
            ,horizontalAlignment = Alignment.CenterHorizontally
        , verticalArrangement = Arrangement.Center
        ) {
            Image(
                bitmap = imgBit,
                contentDescription = "Imagen Obtenida",
                modifier = Modifier.fillMaxSize(0.9f),
                alignment = Alignment.Center
            )

        }


    }
}