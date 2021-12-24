package com.kalex.sp_aplication.presentation

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
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
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ActivityScenario.launch

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.kalex.sp_aplication.R
import com.kalex.sp_aplication.common.Constants
import com.kalex.sp_aplication.presentation.composables.ButtonText
import com.kalex.sp_aplication.presentation.composables.Icono
import com.kalex.sp_aplication.presentation.composables.Imagen

import com.kalex.sp_aplication.presentation.theme.SPAplicationTheme
import com.kalex.sp_aplication.presentation.theme.blanco
import com.kalex.sp_aplication.presentation.theme.spcolor
import com.kalex.sp_aplication.presentation.ui.*
import com.kalex.sp_aplication.presentation.validations.Emailvalidation
import com.kalex.sp_aplication.presentation.viewModels.DataViewModel
import com.kalex.sp_aplication.presentation.viewModels.UserViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class MainActivity : ComponentActivity () {
    lateinit var viewModel :UserViewModel
    private var cancellationSignal: CancellationSignal? = null
    @ExperimentalPermissionsApi
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState,)

        setContent {
            viewModel  = hiltViewModel()
            SPAplicationTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Constants.MainNavItem)
                {
                    composable(Constants.MainNavItem){
                        SingIn(
                            navController,
                            viewModel,
                            onfiger = { launchBiometric() }
                        )
                    }
                    composable(
                        Constants.HomeNavItem
                    ){
                            backStackEntry->
                        val nombre= backStackEntry.arguments?.getString("nombre")
                        requireNotNull(nombre)
                        Home(navController,nombre)

                    }
                    composable(Constants.SendDocNavItem){
                        EnviarDocumento(navController)
                    }
                    composable(Constants.getDocNavItem){
                        VerDocumentos(navController)
                    }
                    composable(Constants.getDocDetailNavItem){
                            backStackEntry->
                        val idRegistro= backStackEntry.arguments?.getString("idRegistro")
                        requireNotNull(idRegistro)
                        VerDocumento(navController, idRegistro = idRegistro)
                    }
                    composable(Constants.oficesNavItem){
                        VerOficinas(navController)
                    }
                }

            }
        }
    }

    private  val authenticationCallback: BiometricPrompt.AuthenticationCallback =
        @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                        viewModel.getUserHuella()

                Toast.makeText(this@MainActivity, "Huella correcta", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@MainActivity, "Authentication Error code: $errorCode", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                super.onAuthenticationHelp(helpCode, helpString)
            }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkBiometricSupport(): Boolean {
        val keyGuardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyGuardManager.isDeviceSecure) {
            return true
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            return false
        }

        return packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun launchBiometric() {
        if (checkBiometricSupport()) {
            val biometricPrompt = BiometricPrompt.Builder(this)
                .apply {
                    setTitle(getString(R.string.prompt_info_title))
                    setSubtitle(getString(R.string.prompt_info_subtitle))
                    setDescription(getString(R.string.prompt_info_description))
                    setConfirmationRequired(false)
                    setNegativeButton(getString(R.string.prompt_info_use_app_password), mainExecutor, { _, _, ->
                        Toast.makeText(this@MainActivity, "Authentication Cancelled", Toast.LENGTH_SHORT).show()
                    })
                }.build()
            biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)

        }

    }



    private  fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            Toast.makeText(this, "Authentication Cancelled Signal", Toast.LENGTH_SHORT).show()
        }

        return cancellationSignal as CancellationSignal
    }

}

@Composable
fun SingIn(
    navController: NavController,
    viewModel : UserViewModel ,
    onfiger: () -> Unit,
    dataviewmodel : DataViewModel = hiltViewModel()
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

        //var resp = viewModel.state.value
        Buttonin(habilitado = text.valid(),viewModel,navController,text.correo,password.value )

        //BOTTON PARA LA HUELLA
        OutlinedButton(
            onClick = {
                onfiger()

                      },
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(0.8f),
            border = BorderStroke(1.dp, Color.Black),
            contentPadding = PaddingValues(12.dp),
            shape = RoundedCornerShape(23.dp),
            //enabled = habilitado
        ) {
            var resp = viewModel.state.value

            if(resp.user != null){
                resp.user?.let {user ->
                    val acceso = user.acceso
                    println("acceso $acceso")
                    // println("respuesta${resp.user}")
                    if (acceso == true){

                        navController.navigate("home/${resp.user?.nombre}")

                    }else if(acceso == false){
                        //Toast.makeText(context,"El Correo o la Contraseña son incorrectos",Toast.LENGTH_LONG).show()

                    }
                }
            }

            Icono(R.drawable.baseline_fingerprint_24,35)
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            ButtonText("Ingresar con huella",22)
        }

        /*ButtonHuella(text.valid()){
            onfiger()
        }//,onfinger*/
    }


}

@Composable
fun Textfield(texto: String ) {
    Text(text = texto,
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.secondary
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
                textColor = MaterialTheme.colors.secondary
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
        style = TextStyle(color = MaterialTheme.colors.error)
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
            textColor = MaterialTheme.colors.secondary
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
    contraseña :String
) {
    val context = LocalContext.current
    Button(
        onClick = {
            viewModel.getUser(correo,contraseña)

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
        var resp = viewModel.state.value
        if(resp.user!= null){

            //println("Respuesta de server: $resp")
            resp.user?.let {user ->
                val acceso = user.acceso
                println("acceso $acceso")
                // println("respuesta${resp.user}")
                if (acceso == true){

                    Toast.makeText(context,"Acceso concedido",Toast.LENGTH_LONG).show()
                    viewModel.saveAll(nombre = user.nombre,correo = correo, contraseña = contraseña)
                    navController.navigate("home/${resp.user?.nombre}")

                }else if(acceso == false){
                    Toast.makeText(context,"El Correo o la Contraseña son incorrectos",Toast.LENGTH_LONG).show()

                }

            }
            resp.user = null
        }


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
}

