package com.kalex.sp_aplication.presentation.composables

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.sp_aplication.R
import com.kalex.sp_aplication.common.getCapturedImage
import com.kalex.sp_aplication.common.getGalleryImage
import com.kalex.sp_aplication.presentation.ui.BtnEnviarImg
import com.kalex.sp_aplication.presentation.ui.BtncargarImg
import com.kalex.sp_aplication.presentation.ui.EMPTY_IMAGE_URI
import com.kalex.sp_aplication.presentation.ui.InputText
import com.kalex.sp_aplication.presentation.ui.assetsToBitmap
import com.kalex.sp_aplication.presentation.ui.capturaraImg
import com.kalex.sp_aplication.presentation.ui.capturaraImgGaleria
import com.kalex.sp_aplication.presentation.ui.dropDownMenu
import com.kalex.sp_aplication.presentation.ui.toBase64String
import com.kalex.sp_aplication.presentation.validations.validatorString
import com.kalex.sp_aplication.presentation.viewModels.PostDocumentViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object FormConstants {
    val documentTypeLIst = listOf("CC", "TI", "CE", "PA")
    val fileTypeList = listOf("Certificado de cuenta", "Cédula", "Factura", "Incapacidad")
}

@ExperimentalPermissionsApi
@Composable
fun DocumentationForm(
    cities: ArrayList<String>,
    email: String,
    postDocumentViewModel: PostDocumentViewModel,
    localFocusManager: FocusManager = LocalFocusManager.current,

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // para hacer scroll
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // ------------------------------------Formulario---------------------------------

        Spacer(Modifier.size(4.dp))

        val menu1 = dropDownMenu(FormConstants.documentTypeLIst, nombreInput = "Tipo de Documento")
        val text1: String = InputText(label = "Numero de documento", onAction = {
            // bajar al siguiente field
            localFocusManager.moveFocus(FocusDirection.Down)
        })
        val text2 = InputText(label = "Nombre", onAction = {
            // bajar al siguiente field
            localFocusManager.moveFocus(FocusDirection.Down)
        })
        val text3 = InputText(label = "Apellido", onAction = {
            // bajar al siguiente field
            localFocusManager.moveFocus(FocusDirection.Down)
        })
        val text4 = InputText(label = "email", email, onAction = {
            // bajar al siguiente field
            localFocusManager.moveFocus(FocusDirection.Down)
        })
        val menu2 = dropDownMenu(cities, nombreInput = "Ciudad")
        val menu3 = dropDownMenu(FormConstants.fileTypeList, nombreInput = "Tipo de Adjunto")

        // ------------------------------------Para la foto---------------------------------
        var takePhotoFlag = remember {
            mutableStateOf(false)
        }
        var getPhotoFlag = remember {
            mutableStateOf(false)
        }
        val context = LocalContext.current
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(4.dp),
        ) {
            getPhotoFlag.value = BtncargarImg("Cargar img", R.drawable.cloud_upload_24)

            Spacer(Modifier.size(ButtonDefaults.IconSpacing))

            takePhotoFlag.value = BtncargarImg("Tomar foto", R.drawable.add_a_photo_24)
        }

        var UriImg: Uri

        // inicializar variable Bitmap
        var imgBitmap: Bitmap? = assetsToBitmap("ic_launcher_background", context)

// ----------------------Obtener foto desde La CAMARA---------------------------
        if (takePhotoFlag.value && !getPhotoFlag.value) {
            UriImg = capturaraImg(
                modifier = Modifier,
            )
            if (UriImg != EMPTY_IMAGE_URI) {
                imgBitmap = getCapturedImage(UriImg)
            }
        }
// ----------------------Obtener foto desde Galeria---------------------------
        if (getPhotoFlag.value) {
            UriImg = capturaraImgGaleria(
                modifier = Modifier,
            )
            if (UriImg != EMPTY_IMAGE_URI) {
                imgBitmap = getGalleryImage(UriImg, context)
            }
        }

// -------------------validaciones para habilitar enviar data---------------------------
        var validacion: Boolean = false
        if (imgBitmap != null) {
            validacion =
                validatorString(text1) && validatorString(text2) && validatorString(text3) && validatorString(
                    text4,
                ) && validatorString(menu1) && validatorString(menu2) && validatorString(menu3)
        }

// -------------------Armado del body para Post Document---------------------------
        // Crear Body para mandar
        var requestBody: RequestBody? = null

        if (validacion) {
// -------------------Convertir Base 64---------------------------
            val imgBse64 = imgBitmap?.toBase64String()
            val imgBase64Encabezado = "data:image/jpeg;base64," + imgBse64

            // Create JSON using JSONObject
            val jsonObject = JSONObject()
            jsonObject.put("TipoId", menu1)
            jsonObject.put("Identificacion", text1)
            jsonObject.put("Nombre", text2)
            jsonObject.put("Apellido", text3)
            jsonObject.put("Ciudad", menu2)
            jsonObject.put("email", text4)
            jsonObject.put("TipoAdjunto", menu3)
            jsonObject.put("Adjunto", imgBase64Encabezado)

            var jsonObjectString = jsonObject.toString()

            requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
        }

        BtnEnviarImg(validacion, postDocumentViewModel, requestBody)
    }
}
