package com.kalex.sp_aplication.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.ui.geometry.Size
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.sp_aplication.R
import com.kalex.sp_aplication.camara.CameraCapture
import com.kalex.sp_aplication.presentation.composables.ButtonText
import com.kalex.sp_aplication.presentation.composables.Drawer
import com.kalex.sp_aplication.presentation.composables.Icono
import com.kalex.sp_aplication.presentation.composables.Imagen
import com.kalex.sp_aplication.presentation.theme.blanco
import com.kalex.sp_aplication.presentation.theme.spcolor
import com.kalex.sp_aplication.presentation.validations.getFileSizeFloat
import com.kalex.sp_aplication.presentation.validations.validarString
import com.kalex.sp_aplication.presentation.viewModels.OficesViewModel
import com.kalex.sp_aplication.presentation.viewModels.PostDocumentViewModel
import com.kalex.usodecamara.galeria.GallerySelect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import com.kalex.sp_aplication.common.getCapturedImage
import java.io.ByteArrayOutputStream
import android.util.Base64
import com.kalex.sp_aplication.common.getGaleryImage
import java.io.IOException
import java.io.InputStream
import kotlin.collections.ArrayList


@ExperimentalPermissionsApi
@Composable
fun EnviarDocumento(
    navController: NavHostController,
    oficesViewModel: OficesViewModel = hiltViewModel(),
    postDocumentViewModel : PostDocumentViewModel = hiltViewModel()
) {

    val resp = oficesViewModel.state.value
    val correo = oficesViewModel.correo
    //para barra lateral
    val scaffoldState = rememberScaffoldState(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    )
    val scope = rememberCoroutineScope()

    val sendDoc = postDocumentViewModel.state
    println("repsuesta del PUT  $sendDoc")

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
    //Logica para obtener ciudades de la API
    if (!resp.isLoading) {
        val ciudades = ArrayList<String>()
        for (ciudad in resp.ofices?.Items!!) {
            if (!ciudades.contains(ciudad.Ciudad)){
            ciudades.add(ciudad.Ciudad)}
        }

        ToolBar(
            navController,
            ciudades,
            scope,
            scaffoldState,
            correo,
            postDocumentViewModel
        )

    }
}
@ExperimentalPermissionsApi
@Composable
fun ToolBar(
    navController :NavHostController,
    ciudades:ArrayList<String>,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    correo: String,
    postDocumentViewModel : PostDocumentViewModel
){
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
        TopAppBar(
            title = { Text(text = "Envio de Documentación") },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }
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
        drawerContent = { Drawer(scope, scaffoldState, navController) },
        drawerGesturesEnabled = true

        ) {
        FormularioDoc(ciudades,correo,postDocumentViewModel)
    }
}

@ExperimentalPermissionsApi
@Composable
fun FormularioDoc(
    ciudades: ArrayList<String>,
    correo: String,
    postDocumentViewModel: PostDocumentViewModel

) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),// para hacer scroll
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
            ){

        //------------------------------------Formulario---------------------------------
        val listaDocumento = listOf("CC", "TI", "CE", "PA")
        val listaTipoAdjunto = listOf("Certificado de cuenta", "Cédula", "Factura", "Incapacidad")

        Spacer(Modifier.size(4.dp))

        val menu1= dropDownMenu(listaDocumento, nombreInput = "Tipo de Documento")

        val text1 :String = InputText(label = "Numero de documento")
        val text2 = InputText(label = "Nombre")
        val text3 = InputText(label = "Apellido")
        val text4 = InputText(label = "Correo",correo)

        val menu2=dropDownMenu(ciudades, nombreInput = "Ciudad")
        val menu3=dropDownMenu(listaTipoAdjunto, nombreInput = "Tipo de Adjunto")

       //------------------------------------Para la foto---------------------------------
        var tomarFoto =  false
        var cargarFoto =  false
        val context = LocalContext.current
        Row(
            verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement=Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(4.dp)
            ){
            cargarFoto= BtncargarImg("Cargar img")

                Spacer(Modifier.size(ButtonDefaults.IconSpacing))

                 tomarFoto= BtncargarImg("Tomar foto")

            }

        var UriImg :Uri = Uri.parse("file://dev/null")

        //inicializar variable Bitmap
        var imgBitmap :Bitmap? = assetsToBitmap("ic_launcher_background",context)


        println("Presiono botton foto : "+tomarFoto)
        println("Presiono botton galeria : "+cargarFoto)

//----------------------Obtener foto desde La CAMARA---------------------------
        if(tomarFoto && !cargarFoto){
            UriImg = capturaraImg(
                modifier = Modifier,
            )
            if(UriImg != EMPTY_IMAGE_URI ) {
                imgBitmap = getCapturedImage(UriImg)
                //println("El tamaño de la foto de galeria es :" + UriImg.toFile().getFileSizeFloat())
              /*
                if (imgBitmap != null) {
                    println("Cantidad de bytes Camara: " + imgBitmap!!.getByteCount())

                }*/
            }
        }
//----------------------Obtener foto desde Galeria---------------------------
        if (cargarFoto){
            UriImg = capturaraImgGaleria(
                modifier = Modifier,
            )

            if(UriImg != EMPTY_IMAGE_URI ) {
                imgBitmap = getGaleryImage(UriImg, context)

            }
        }




//-------------------validaciones para habilitar enviar data---------------------------
        var validacion :Boolean = false
        if (imgBitmap != null){
           validacion = validarString(text1)&&validarString(text2)&&validarString(text3) &&validarString(text4)&&validarString(menu1)&&validarString(menu2)&&validarString(menu3)

        }

//-------------------Armado del body para Post Document---------------------------
        //Crear Body para mandar
        var requestBody : RequestBody? =null

        if(validacion){
//-------------------Convertir Base 64---------------------------
            val imgBse64 = imgBitmap?.toBase64String()
            val imgBase64Encabezado = "data:image/jpeg;base64,"+ imgBse64

            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("TipoId", menu1)
            jsonObject.put("Identificacion", text1)
            jsonObject.put("Nombre", text2)
            jsonObject.put("Apellido", text3)
            jsonObject.put("Ciudad", menu2)
            jsonObject.put("Correo", text4)
            jsonObject.put("TipoAdjunto", menu3)
            jsonObject.put("Adjunto", imgBase64Encabezado)

            var jsonObjectString = jsonObject.toString()

            requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        }

        BtnEnviarImg(validacion,postDocumentViewModel,requestBody)

    }

}

//Convertir a Bitmap desde el file name NO SE USA , PERO ES INTEREZANTE
fun assetsToBitmap(fileName:String,context : Context): Bitmap?{

    return try{
        val stream : InputStream  = context.assets.open(fileName)
        BitmapFactory.decodeStream(stream)
    }catch (e: IOException){
        e.printStackTrace()
        null
    }
}


// extension function to encode bitmap to base64 string
fun Bitmap.toBase64String():String{
    ByteArrayOutputStream().apply {
        compress(Bitmap.CompressFormat.JPEG,90,this)
        return Base64.encodeToString(toByteArray(),Base64.DEFAULT)
    }
}

@Composable
fun InputText(
label : String,
initialValue :String=""
):String{
    var text by remember { mutableStateOf(initialValue ) }
    TextField(
        value = text,
        singleLine = true,
        onValueChange = { text = it },
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .background(Color.White),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent
        )
    )
        return text
}

@Composable
fun dropDownMenu(
    listaDocumento: List<String>,
    nombreInput:String):String
{

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
        return selectedText
}

@Composable
fun BtnEnviarImg(
    validacion: Boolean,
    postDocumentViewModel: PostDocumentViewModel,
    requestBody: RequestBody?
) {
    Button(
        onClick = {
            requireNotNull(requestBody)
            postDocumentViewModel.postDocument(requestBody)

            runBlocking {
                delay(100)
            }
            var resp = postDocumentViewModel.state
            println(resp.value)

        },
        modifier = Modifier
            .padding(top = 25.dp, bottom = 12.dp)
            .fillMaxWidth(0.8f)
        ,
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(23.dp),
        contentPadding = PaddingValues(9.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = spcolor,
            contentColor = blanco
        ),
        enabled = validacion
    ) {

        Icono(R.drawable.outline_login_24,25)
        Spacer(Modifier.size(4.dp))
        ButtonText("Enviar",20)

    }
}

@ExperimentalPermissionsApi
@Composable
fun BtncargarImg(
    texto: String,
):Boolean{

    var click by remember { mutableStateOf(false) }
    Button(
        onClick = {
            click = true
        },
        modifier = Modifier
            .padding(top = 23.dp)
            ,
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(9.dp),
        contentPadding = PaddingValues(8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = spcolor,
            contentColor = blanco
        ),
        enabled = true
    ) {

        Icono(R.drawable.outline_login_24,25)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        ButtonText(texto,15)

    }

    return click
}

//funcion para capturar img y validar su tamaño
@ExperimentalPermissionsApi
@Composable
fun capturaraImg(
    modifier: Modifier = Modifier,
    onimagenTomada: (Uri) -> Unit = { }
):Uri {
    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }
    val context = LocalContext.current
    if (imageUri != EMPTY_IMAGE_URI) {
        onimagenTomada(imageUri)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Imagen(
                imageUri, modifier = Modifier
                    .height(400.dp)
                    .width(500.dp)
                    .padding(10.dp)
            )

            Button(
                modifier = Modifier,
                onClick = {
                    imageUri = EMPTY_IMAGE_URI
                }
            ) {
                Text("Eliminar imagen")
            }
        }


    } else {

            CameraCapture(
                modifier = modifier,
                onImageFile = { file ->
                    imageUri = file.toUri()
                    //-------------------validaciones para tamaño de imagenes---------------------------
                    val uriSize = imageUri.toFile().getFileSizeFloat()
                    if (uriSize > 1000) {
                        Toast.makeText(
                            context,
                            "Imagen demaciado pesada,toma otra foto",
                            Toast.LENGTH_LONG
                        ).show()
                        imageUri = EMPTY_IMAGE_URI
                    }
                }
            )
    }
    return imageUri
}

//funcion para capturar img y validar su tamaño
@ExperimentalPermissionsApi
@Composable
fun capturaraImgGaleria(
    modifier: Modifier = Modifier,
    onimagenTomada: (Uri) -> Unit = { }
):Uri {
    var imageUri by remember { mutableStateOf(EMPTY_IMAGE_URI) }

    if (imageUri != EMPTY_IMAGE_URI) {
        onimagenTomada(imageUri)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Imagen(
                imageUri, modifier = Modifier
                    .height(400.dp)
                    .width(500.dp)
                    .padding(10.dp)
            )

            Button(
                modifier = Modifier,
                onClick = {
                    imageUri = EMPTY_IMAGE_URI
                }
            ) {
                Text("Eliminar imagen")
            }
        }


    } else {
        GallerySelect(
            modifier = modifier,
            onImageUri = { uri ->
                imageUri = uri
                //-------------------validaciones para tamaño de imagenes---------------------------
            }
        )
    }
    return imageUri
}

val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")

