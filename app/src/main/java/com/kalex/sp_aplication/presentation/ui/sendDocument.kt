package com.kalex.sp_aplication.presentation.ui

import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavHostController
import com.kalex.sp_aplication.presentation.composables.ButtonText


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
            title = { Text(text = "Envio de Documentación") },
            navigationIcon ={
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
            //backgroundColor = Color.White
        )
    }) {
        FormularioDoc(navController )
    }
}

@Composable
fun FormularioDoc(navController :NavHostController) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),// para hacer scroll
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
            ){
        val listaDocumento = listOf("CC", "TI", "CE", "PA")
        val listaTipoAdjunto = listOf("Certificado de cuenta", "Cédula", "Factura", "Incapacidad")

        dropDownMenu(listaDocumento, nombreInput = "Tipo de Documento")
        InputText(label = "Numero de documento")
        InputText(label = "Nombre")
        InputText(label = "Apellido")
        ButtonText(msg="Aqui va el correo")
        dropDownMenu(listaDocumento, nombreInput = "Ciudad")
        dropDownMenu(listaTipoAdjunto, nombreInput = "Tipo de Adjunto")

    }
}

@Composable
fun InputText(
label : String
){
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(0.9f)
            .background(Color.White),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        )
    )
}

@Composable
fun dropDownMenu(listaDocumento: List<String>,
                 nombreInput:String) {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listaDocumento
    var selectedText by remember { mutableStateOf("") }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column {
    TextField(
        value = selectedText,
        onValueChange = { selectedText = it },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .onGloballyPositioned { coordinates ->
                //This value is used to assign to the DropDown the same width
                textfieldSize = coordinates.size.toSize()
            }
            .background(Color.White),
        label = { Text(nombreInput) },
        trailingIcon = {
            Icon(icon, "contentDescription",
                Modifier.clickable { expanded = !expanded })
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        )
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
    ) {
        suggestions.forEach { label ->
            DropdownMenuItem(onClick = {
                selectedText = label
                expanded = false
            }) {
                Text(text = label)
            }
        }
    }
}

}
