package com.kalex.sp_aplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kalex.sp_aplication.ui.theme.blanco
import com.kalex.sp_aplication.ui.theme.gris
import com.kalex.sp_aplication.ui.theme.spcolor
import org.intellij.lang.annotations.JdkConstants

@Composable fun SingIn(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogoSP()
        Textfield("Ingresa tus datos para acceder")
        EmailField()
        PasswordFiels()
        Button()
        ButtonHuella()
    }


}



@Composable
fun LogoSP() {
    Image(
        painterResource(id = R.drawable.logo_sophos_home) ,
        contentDescription = "Logo SP",
        modifier = Modifier
            .height(210.dp)
            .width(360.dp)
            .padding(20.dp)


    )
}

@Composable
fun Textfield(texto: String ) {
    Text(text = texto,
        style = MaterialTheme.typography.h6,
        color = colors.secondary
    )
}

@Composable
fun EmailField() {
    var text = remember { mutableStateOf(TextFieldValue()) }

    TextField(
        value = text.value,
        onValueChange = { text.value = it },
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email) ,

    )
}

@Composable
fun PasswordFiels() {
    var password = remember { mutableStateOf(TextFieldValue()) }

    TextField(
        value = password.value,
        onValueChange = { password.value = it },
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
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)


    )
}

@Composable
fun Button() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(top = 30.dp)
            .fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(23.dp),
        contentPadding = PaddingValues(12.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = spcolor,
            contentColor = blanco
        )
    ) {
        Icon(
            Icons.Filled.Done,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = "Ingresar",
            fontSize = 25.sp,
            //fontFamily = FontFamily.SansSerif
        )

    }
}


@Composable
fun ButtonHuella() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Black),
        contentPadding = PaddingValues(12.dp),
        shape = RoundedCornerShape(23.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = spcolor,
            contentColor = blanco
        )
    ) {
        Icon(
            Icons.Filled.ThumbUp,
            contentDescription = null,
            modifier = Modifier.size(ButtonDefaults.IconSize)
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = "Ingresar con huella",
            fontSize = 25.sp,

        )

    }
}

