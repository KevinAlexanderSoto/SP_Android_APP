package com.kalex.sp_aplication.ui


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.unit.dp
import com.kalex.sp_aplication.R
import com.kalex.sp_aplication.composables.ButtonText
import com.kalex.sp_aplication.composables.Icono
import com.kalex.sp_aplication.composables.IconoVector
import com.kalex.sp_aplication.composables.Imagen
import com.kalex.sp_aplication.theme.blanco
import com.kalex.sp_aplication.theme.spcolor


@Composable fun Home(){
    ToolBar(nombre = "Kevin")

}
@Composable
fun ToolBar(
    nombre:String
){
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = nombre)},
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Menu,
                        contentDescription = "menu hamburgesa")
                }
            }
        )


    }) {
        padding->
        Contenido()

    }
}

@Composable
fun Contenido(
){
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),// para hacer scroll
        verticalArrangement = Arrangement.spacedBy(19.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Imagen(url = R.drawable.homeimg, modifier = Modifier
            .wrapContentSize(Alignment.BottomCenter)
            .height(246.dp)
            .width(440.dp)
            .padding(2.dp) )
        Card(encabezado = "Enviar Documentos",R.drawable.outline_login_24)
        Card(encabezado = "Ver Documentos",R.drawable.outline_login_24)
        Card(encabezado = "Oficinas",R.drawable.outline_login_24)
        Spacer(modifier = Modifier.padding(3.dp))
    }
}

@Composable
fun Card(
    encabezado:String,
    urlIcono :Any
){
    Card(
        elevation = 8.dp,
        //border = BorderStroke(0.5.dp, Color.Black),
        shape = RoundedCornerShape(17.dp),
        modifier = Modifier
            .fillMaxWidth(0.9f)

    ){
        Column (
            modifier = Modifier.padding(15.dp)
                .fillMaxWidth()
        ){

            Row(modifier = Modifier.padding(2.dp,1.dp,5.dp,20.dp)) {
                Icono(urlIcono,30)
                Spacer(modifier = Modifier.padding(3.dp))
                Text(text = encabezado)
            }

            Column(horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                ) {
                Button(onClick = { /*TODO*/ },
                    shape = RoundedCornerShape(24.dp),
                    contentPadding = PaddingValues(15.dp,8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = spcolor,
                        contentColor = blanco
                    )
                ) {
                    ButtonText( "Ingresar",15)
                    IconoVector(Icons.Default.ArrowForward,20)
                }
            }

        }
        
    }
}

