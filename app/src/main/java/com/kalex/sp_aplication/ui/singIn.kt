package com.kalex.sp_aplication.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.kalex.sp_aplication.Emailvalidation
import com.kalex.sp_aplication.R
import com.kalex.sp_aplication.composables.ButtonText
import com.kalex.sp_aplication.composables.Icono
import com.kalex.sp_aplication.composables.Imagen
import com.kalex.sp_aplication.theme.blanco
import com.kalex.sp_aplication.theme.spcolor


@Composable fun SingIn(

    signin : (email : String ,password :String) -> Boolean?

){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),// para hacer scroll
        verticalArrangement = Arrangement.spacedBy(11.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Imagen(url = R.drawable.logo_sophos_home, modifier = Modifier
            .height(210.dp)
            .width(350.dp)
            .padding(15.dp) )

        Textfield("Ingresa tus datos para acceder")

        // manejar focus de los texto,
        val localFocusManager = LocalFocusManager.current

        //state hoisting Email
        val text = remember { Emailvalidation() }

        EmailField(
            text.text,
            text.error,
            onAction = {
                // bajar al siguiente field
                localFocusManager.moveFocus(FocusDirection.Down)
            }
        ){

            text.text = it
            text.validate()
        }

        //state hoisting Password
        var password = remember { mutableStateOf("") }
        PasswordFiels(password.value, onAction ={ localFocusManager.clearFocus()})
        { password.value = it
        }

        Buttonin(habilitado = text.valid() ){
            signin(text.text,password.value)
        }

        ButtonHuella(text.valid())
    }


}

@Composable
fun Textfield(texto: String ) {
    Text(text = texto,
        style = MaterialTheme.typography.h6,
        color = colors.secondary
    )
}

@Composable
fun EmailField(
    text: String,
    error: String?,
    onAction: () -> Unit,
    onEmailChanged: (String) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


    TextField(
        value = text,
        singleLine = true,
        onValueChange = { onEmailChanged(it)},
        label = { Text(text = "Email") },
        placeholder = { Text("example@gmail.com") },
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(0.9f),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            textColor = colors.secondary
        ),
        shape = RoundedCornerShape(9.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
            ) ,
        keyboardActions = KeyboardActions (onNext = {
            onAction()
        }),
        isError = error != null
    )
        error?.let{ FielError(it) }
    }
}

@Composable
fun FielError(it: String) {
    Text(
        text = it,
        style = TextStyle(color =colors.error)
    )
}

@Composable
fun PasswordFiels(
    pass: String,
    onAction: () -> Unit,
    onPasswordChange: (String) -> Unit
) {
    TextField(
        value = pass,
        onValueChange = { onPasswordChange(it) },
        label = { Text("Contraseña" ) },
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(0.9f),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            textColor = colors.secondary
        ),
        shape = RoundedCornerShape(9.dp),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ) ,
        keyboardActions = KeyboardActions (onDone = {
            onAction()
        }),

    )
}

@Composable
fun Buttonin(habilitado: Boolean,signin :()->Boolean?) {
    val context = LocalContext.current
    Button(
        onClick = {
            var acceso = signin()
                  if (acceso == true){
                      Toast.makeText(context,"Acceso concedido",Toast.LENGTH_LONG).show()

                  }else if(acceso == false){
                      Toast.makeText(context,"Acceso Denegado",Toast.LENGTH_LONG).show()

                  }

                  },
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth(0.8f)
        ,
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(23.dp),
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = spcolor,
            contentColor = blanco
        ),
        enabled = habilitado
    ) {

        Icono(R.drawable.outline_login_24,30)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        ButtonText("Ingresar",22)

    }
}


@Composable
fun ButtonHuella(habilitado : Boolean) {
    OutlinedButton(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Black),
        contentPadding = PaddingValues(12.dp),
        shape = RoundedCornerShape(23.dp),
        enabled = habilitado
    ) {
        Icono(R.drawable.baseline_fingerprint_24,35)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        ButtonText("Ingresar con huella",22)


    }
}



