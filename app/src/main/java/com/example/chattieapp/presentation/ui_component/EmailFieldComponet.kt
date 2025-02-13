package com.example.chattieapp.presentation.ui_component

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chattieapp.R
import com.example.chattieapp.presentation.navigation.Screen
import com.example.chattieapp.presentation.sign_in_screen.AuthState

@Composable
fun EmailFieldComponet(
    modifier: Modifier = Modifier,
    emailText: String,
    onEmailInput: (String) -> Unit,
    labelText: String,
    leadingIconRes: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next
) {
    var isEmailValid by remember { mutableStateOf(true) }


    OutlinedTextField(
        value = emailText,
        onValueChange = {
            onEmailInput(it)
            isEmailValid = isValidEmail(it)

        },
        isError = !isEmailValid,  // Hata durumunu göster
        label = {
            Text(
                text = labelText,
                style = TextStyle(color = colorResource(id = R.color.gray))
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIconRes),
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(24.dp)
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
    )
}

@Composable
fun PasswordFieldComponent(
    modifier: Modifier = Modifier,
    passwordText: String,
    onPasswordInput: (String) -> Unit,
    labelText: String,
    leadingIconRes: Int,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    isPasswordField: Boolean = false
) {

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    OutlinedTextField(
        value = passwordText,
        onValueChange = {
            // Eğer yeni şifre 11 karakterden uzun değilse, girişi kabul et
            if (it.length <= 11) {
                onPasswordInput(it)
            } else {
                // Eğer 11 karakteri geçtiyse, girişi engelle
                Toast.makeText(context, "Password must be exactly 11 characters", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(id = leadingIconRes),
                contentDescription = null,
                tint = Color.LightGray,
                modifier = modifier.size(24.dp)
            )
        },
        label = {
            Text(text = labelText, style = TextStyle(color = colorResource(id = R.color.gray)))
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        trailingIcon = {
            if (isPasswordField) {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        painter = painterResource(id = if (isPasswordVisible) R.drawable.hide else R.drawable.show),
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = modifier.size(24.dp)
                    )

                }
            }
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}


@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    onClick: ()->Unit,
    buttonText : String
) {

    val isClicked = remember {
        mutableStateOf(false)
    }

    Button(
        onClick = {
            isClicked.value = !isClicked.value
            if (isClicked.value == true) {
                onClick()
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            2.dp,
            color = colorResource(id = if (isClicked.value) R.color.app_color else R.color.gray)
        ),
        content = {
            Text(
                text = buttonText,
                style = TextStyle(
                    color = Color.Black,
                )
            )
        }
    )
}
fun isValidEmail(email: String): Boolean { //girilen emailin email formatında olup olmadığını kontrol edecek
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
