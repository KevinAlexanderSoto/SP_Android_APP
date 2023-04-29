package com.kalex.sp_aplication.presentation.ui

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.kalex.sp_aplication.authentication.FingerPrintAuthentication
import com.kalex.sp_aplication.presentation.composables.ButtonText
import com.kalex.sp_aplication.presentation.composables.Icon
import com.kalex.sp_aplication.presentation.composables.Image
import com.kalex.sp_aplication.presentation.theme.blanco
import com.kalex.sp_aplication.presentation.theme.spcolor
import com.kalex.sp_aplication.presentation.validations.EmailValidation
import com.kalex.sp_aplication.presentation.validations.states.UserState
import com.kalex.sp_aplication.presentation.viewModels.AuthenticationViewModel
import com.kalex.sp_aplication.presentation.viewModels.DataViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun SingIn(
    navController: NavController,
) {
    val context = LocalContext.current
    val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
    val storedDataViewModel: DataViewModel = hiltViewModel()
    val fingerPrintAuthentication: FingerPrintAuthentication = hiltViewModel()
    val localFocusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()), // para hacer scroll
        verticalArrangement = Arrangement.spacedBy(11.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            url = R.drawable.logo_sophos_home,
            modifier = Modifier
                .height(210.dp)
                .width(350.dp)
                .padding(15.dp),
        )

        TextField("Ingresa tus datos para acceder")

        // state hoisting Email
        val text = remember { EmailValidation() }

        EmailField(
            text.correo,
            text.error,
            onAction = { localFocusManager.moveFocus(FocusDirection.Down) },
        ) {
            text.correo = it
            text.validate()
        }

        // state hoisting Password
        val password = remember { mutableStateOf("") }
        PasswordFields(
            password.value,
            onAction = { localFocusManager.clearFocus() },
        ) { password.value = it }

        ButtonIn(
            habilitado = text.valid(),
        ) {
            authenticationViewModel.getUser(text.correo, password.value)
            handleAuthenticationState(authenticationViewModel.state.value) {
                storedDataViewModel.saveAll(it, text.correo, password.value)
                navController.navigate("home/$it")
            }
            // TODO: Take a look to the access in the user model response
        }
        val coroutineScope = rememberCoroutineScope()
        FingerPrintButton(
            checkStoredCredentials(
                storedDataViewModel.email,
                storedDataViewModel.password,
            ),
        ) {
            fingerPrintAuthentication.launchBiometric()
            coroutineScope.launch {
                fingerPrintAuthentication.authenticationResult.collectLatest {
                    if (it) {
                        authenticationViewModel.getUser(
                            storedDataViewModel.email.value,
                            storedDataViewModel.password.value,
                        )
                        if (!authenticationViewModel.state.value.isLoading) {
                            val currentUserName =
                                authenticationViewModel.state.value.user?.nombre ?: ""
                            storedDataViewModel.saveUserName(currentUserName)
                            navController.navigate("home/$currentUserName")
                        }
                    }
                }
            }
        }
    }
}

fun handleAuthenticationState(value: UserState, onSuccessState: (String) -> Unit) {
    if (value.isLoading) {
        // CircularProgressIndicator()
    } else {
        onSuccessState(value.user?.nombre ?: "")
    }
}

fun checkStoredCredentials(email: State<String>, password: State<String>) =
    email.value.isNotEmpty() && password.value.isNotEmpty()

@Composable
fun TextField(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.h6,
        color = colors.secondary,
    )
}

@Composable
fun EmailField(
    text: String,
    error: String?,
    onAction: () -> Unit,
    onEmailChanged: (String) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextField(
            value = text,
            singleLine = true,
            onValueChange = { onEmailChanged(it) },
            label = { Text(text = "Email") },
            placeholder = { Text("example@gmail.com") },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.9f),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = colors.secondary,
            ),
            shape = RoundedCornerShape(9.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(onNext = {
                onAction()
            }),
            isError = error != null,
        )
        error?.let { FieldError(it) }
    }
}

@Composable
fun FieldError(it: String) {
    Text(
        text = it,
        style = TextStyle(color = colors.error),
    )
}

@Composable
fun PasswordFields(
    pass: String,
    onAction: () -> Unit,
    onPasswordChange: (String) -> Unit,
) {
    TextField(
        value = pass,
        onValueChange = { onPasswordChange(it) },
        label = { Text("Contraseña") },
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(0.9f),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            textColor = colors.secondary,
        ),
        shape = RoundedCornerShape(9.dp),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = {
            onAction()
        }),

        )
}

@Composable
fun ButtonIn(
    habilitado: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = {
            onClick.invoke()
        },
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(23.dp),
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = spcolor,
            contentColor = blanco,
        ),
        enabled = habilitado,
    ) {
        Icon(R.drawable.outline_login_24, 30)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        ButtonText("Ingresar", 22)
    }

}

@Composable
fun FingerPrintButton(habilitate: Boolean, onButtonClick: () -> Unit) { // ,
    OutlinedButton(
        onClick = { onButtonClick() },
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Black),
        contentPadding = PaddingValues(12.dp),
        shape = RoundedCornerShape(23.dp),
        enabled = habilitate,
    ) {
        Icon(R.drawable.baseline_fingerprint_24, 35)
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        ButtonText("Ingresar con huella", 22)
    }
}
