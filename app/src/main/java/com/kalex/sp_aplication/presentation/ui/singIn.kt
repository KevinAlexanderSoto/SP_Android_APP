package com.kalex.sp_aplication.presentation.ui

/*import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kalex.sp_aplication.R
import com.kalex.sp_aplication.presentation.composables.ButtonText
import com.kalex.sp_aplication.presentation.composables.Icono
import com.kalex.sp_aplication.presentation.composables.Imagen
import com.kalex.sp_aplication.presentation.theme.blanco
import com.kalex.sp_aplication.presentation.theme.spcolor
import com.kalex.sp_aplication.presentation.validations.Emailvalidation
import com.kalex.sp_aplication.presentation.viewModels.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File*/


/*
@Composable fun SingIn(
    navController: NavController,
    viewModel : UserViewModel = hiltViewModel(),
    onfiger: () -> Unit
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
            text.correo,
            text.error,
            onAction = {
                // bajar al siguiente field
                localFocusManager.moveFocus(FocusDirection.Down)
            }
        ){

            text.correo = it
            text.validate()
        }

        //state hoisting Password
        var password = remember { mutableStateOf("") }
        PasswordFiels(password.value, onAction ={ localFocusManager.clearFocus()})
        { password.value = it
        }

        viewModel.getUser(text.correo,password.value)
        //var resp = viewModel.state.value
        Buttonin(habilitado = text.valid(),viewModel,navController,text.correo,password.value )

        ButtonHuella(text.valid()){
            onfiger()
        }//,onfinger
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
        label = { Text("Contrase??a" ) },
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
fun Buttonin(
    habilitado: Boolean,
    viewModel: UserViewModel,
    navController: NavController,
    correo: String,
    contrase??a :String
) {
    val context = LocalContext.current
    Button(
        onClick = {
            runBlocking {
                launch {
                    delay(100L)
                }
            }

            var resp = viewModel.state.value

               //println("Respuesta de server: $resp")
                resp.user?.let {user ->
                    val acceso = user.acceso
                    println("acceso $acceso")
                    // println("respuesta${resp.user}")
                    if (acceso == true){

                        Toast.makeText(context,"Acceso concedido",Toast.LENGTH_LONG).show()
                        viewModel.saveAll(nombre = user.nombre,correo = correo, contrase??a = contrase??a)
                        navController.navigate("home/${resp.user?.nombre}")

                    }else if(acceso == false){
                        Toast.makeText(context,"El Correo o la Contrase??a son incorrectos",Toast.LENGTH_LONG).show()

                    }

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
    //if(resp.isLoading) CircularProgressIndicator()
}


@Composable
fun ButtonHuella(habilitado: Boolean,onfiger: () -> Unit) {//,
    OutlinedButton(
        onClick = { onfiger() },
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Black),
        contentPadding = PaddingValues(12.dp),
        shape = RoundedCornerShape(23.dp),
        //enabled = habilitado
    ) {
        Icono(R.drawable.baseline_fingerprint_24,35)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        ButtonText("Ingresar con huella",22)
    }
}*/
