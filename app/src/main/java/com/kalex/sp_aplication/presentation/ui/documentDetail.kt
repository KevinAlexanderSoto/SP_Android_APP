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

        //var doc :String = "image/jpeg;base64,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"

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