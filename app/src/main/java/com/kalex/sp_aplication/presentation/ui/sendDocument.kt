package com.kalex.sp_aplication.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DrawerValue
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.sp_aplication.R
import com.kalex.sp_aplication.camara.CameraCapture
import com.kalex.sp_aplication.galeria.GallerySelect
import com.kalex.sp_aplication.presentation.composables.ButtonText
import com.kalex.sp_aplication.presentation.composables.DocumentationForm
import com.kalex.sp_aplication.presentation.composables.Drawer
import com.kalex.sp_aplication.presentation.composables.Icon
import com.kalex.sp_aplication.presentation.composables.Image
import com.kalex.sp_aplication.presentation.composables.SophosLoadingIndicator
import com.kalex.sp_aplication.presentation.theme.blanco
import com.kalex.sp_aplication.presentation.theme.spcolor
import com.kalex.sp_aplication.presentation.viewModels.OficesViewModel
import com.kalex.sp_aplication.presentation.viewModels.PostDocumentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

@ExperimentalPermissionsApi
@Composable
fun EnviarDocumento(
    navController: NavHostController,
    oficesViewModel: OficesViewModel = hiltViewModel(),
    postDocumentViewModel: PostDocumentViewModel = hiltViewModel(),
) {
    val resp = oficesViewModel.state.value
    val email = oficesViewModel.correo
    // para barra lateral
    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    )
    val scope = rememberCoroutineScope()

    // barra de cargando
    if (resp.isLoading) {
        SophosLoadingIndicator()
    }
    // Logica para obtener ciudades de la API
    if (!resp.isLoading) {
        val cities = ArrayList<String>()
        for (ciudad in resp.ofices?.Items!!) {
            if (!cities.contains(ciudad.Ciudad)) cities.add(ciudad.Ciudad)
        }

        ToolBar(
            navController,
            cities,
            scope,
            scaffoldState,
            email,
            postDocumentViewModel,
        )
    }
}

@ExperimentalPermissionsApi
@Composable
fun ToolBar(
    navController: NavHostController,
    ciudades: ArrayList<String>,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    correo: String,
    postDocumentViewModel: PostDocumentViewModel,
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "Envio de Documentación") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "go back",
                        )
                    }
                },
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
        DocumentationForm(ciudades, correo, postDocumentViewModel)
    }
}


// Convertir a Bitmap desde el file name NO SE USA , PERO ES INTEREZANTE
fun assetsToBitmap(fileName: String, context: Context): Bitmap? {
    return try {
        val stream: InputStream = context.assets.open(fileName)
        BitmapFactory.decodeStream(stream)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

// extension function to encode bitmap to base64 string
fun Bitmap.toBase64String(): String {
    ByteArrayOutputStream().apply {
        compress(Bitmap.CompressFormat.JPEG, 90, this)
        return Base64.encodeToString(toByteArray(), Base64.DEFAULT)
    }
}

@Composable
fun InputText(
    label: String,
    initialValue: String = "",
    onAction: () -> Unit,
): String {
    var text by remember { mutableStateOf(initialValue) }
    TextField(
        value = text,
        singleLine = true,
        onValueChange = { text = it },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Color.White),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = {
            onAction()
        }),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
        ),
    )
    return text
}

@Composable
fun dropDownMenu(
    listaDocumento: List<String>,
    nombreInput: String,
): String {
    var expanded by remember { mutableStateOf(false) }
    val suggestions = listaDocumento
    var selectedText by remember { mutableStateOf("") }

    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    Column {
        TextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .onGloballyPositioned { coordinates ->
                    // This value is used to assign to the DropDown the same width
                    textfieldSize = coordinates.size.toSize()
                }
                .background(Color.White),
            label = { Text(nombreInput) },
            trailingIcon = {
                Icon(
                    icon,
                    "contentDescription",
                    Modifier.clickable { expanded = !expanded },
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
            ),
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() }),
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
    return selectedText
}

@Composable
fun BtnEnviarImg(
    validacion: Boolean,
    postDocumentViewModel: PostDocumentViewModel,
    requestBody: RequestBody?,
) {
    val context = LocalContext.current
    Button(
        onClick = {
            requireNotNull(requestBody)
            postDocumentViewModel.postDocument(requestBody)

            runBlocking {
                delay(100)
            }
        },
        modifier = Modifier
            .padding(top = 25.dp, bottom = 12.dp)
            .fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(23.dp),
        contentPadding = PaddingValues(9.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = spcolor,
            contentColor = blanco,
        ),
        enabled = validacion,
    ) {
        var resp = postDocumentViewModel.state
        resp.value.respuesta
        println(resp.value)
        if (resp.value.respuesta != null) {
            Toast.makeText(context, "Documento Enviado Exitosamente", Toast.LENGTH_LONG).show()
            println(resp.value)
        }

        Icon(R.drawable.send_24, 25)
        Spacer(Modifier.size(4.dp))
        ButtonText("Enviar", 20)
    }
}

@ExperimentalPermissionsApi
@Composable
fun BtncargarImg(
    texto: String,
    icono: Any,
): Boolean {
    var click by remember { mutableStateOf(false) }
    Button(
        onClick = {
            click = true
        },
        modifier = Modifier
            .padding(top = 23.dp),
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(9.dp),
        contentPadding = PaddingValues(8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = spcolor,
            contentColor = blanco,
        ),
        enabled = true,
    ) {
        Icon(icono, 20)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        ButtonText(texto, 15)
    }

    return click
}

// funcion para capturar img
@ExperimentalPermissionsApi
@Composable
fun capturaraImg(
    modifier: Modifier = Modifier,
    onimagenTomada: (Uri) -> Unit = { },
): Uri {
    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
    val context = LocalContext.current
    if (imageUri != EMPTY_IMAGE_URI) {
        onimagenTomada(imageUri)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                imageUri,
                modifier = Modifier
                    .height(400.dp)
                    .width(500.dp)
                    .padding(10.dp),
            )

            Button(
                modifier = Modifier,
                onClick = {
                    imageUri = EMPTY_IMAGE_URI
                },
            ) {
                Text("Eliminar imagen")
            }
        }
    } else {
        CameraCapture(
            modifier = modifier,
            onImageFile = { file ->
                imageUri = file.toUri()
            },
        )
    }
    return imageUri
}

// funcion para capturar img y validar su tamaño
@ExperimentalPermissionsApi
@Composable
fun capturaraImgGaleria(
    modifier: Modifier = Modifier,
    onimagenTomada: (Uri) -> Unit = { },
): Uri {
    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }

    if (imageUri != EMPTY_IMAGE_URI) {
        onimagenTomada(imageUri)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                imageUri,
                modifier = Modifier
                    .height(400.dp)
                    .width(500.dp)
                    .padding(10.dp),
            )

            Button(
                modifier = Modifier,
                onClick = {
                    imageUri = EMPTY_IMAGE_URI
                },
            ) {
                Text("Eliminar imagen")
            }
        }
    } else {
        GallerySelect(
            modifier = modifier,
            onImageUri = { uri ->
                imageUri = uri
            },
        )
    }
    return imageUri
}

val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")
