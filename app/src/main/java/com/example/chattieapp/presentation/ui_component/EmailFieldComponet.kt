package com.example.chattieapp.presentation.ui_component

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    OutlinedTextField(
        value = emailText,
        onValueChange = {
            onEmailInput(it)
        },
        label = {
            Text(
                text = labelText,
                style = TextStyle(color = Color.LightGray)
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

    OutlinedTextField(
        value = passwordText,
        onValueChange = { onPasswordInput(it) },
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
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val isClicked = remember {
        mutableStateOf(false)
    }

    Button(
        onClick = {
            isClicked.value = !isClicked.value
            if (isClicked.value == true) {
                navController.navigate(Screen.SignUpScreen.route)
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
                text = stringResource(id = R.string.login),
                style = TextStyle(
                    color = Color.Black,
                )
            )
        }
    )
}
