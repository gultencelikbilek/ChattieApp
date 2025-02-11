package com.example.chattieapp.presentation.sign_in_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chattieapp.R
import com.example.chattieapp.presentation.ui_component.ButtonComponent
import com.example.chattieapp.presentation.ui_component.EmailFieldComponet
import com.example.chattieapp.presentation.ui_component.PasswordFieldComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    var emailText = remember {
        mutableStateOf("")
    }
    var passwordText = remember {
        mutableStateOf("")
    }

    Scaffold(
       topBar = {
                 TopAppBar(title = { },
                     colors = TopAppBarDefaults.topAppBarColors(
                         containerColor = colorResource(id = R.color.app_color)
                     ))
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier
               ) {
                    Image(
                        painter = painterResource(id = R.drawable.chat_logo),
                        contentDescription = ""
                    )
                }

                Column(
                    modifier = Modifier.padding(top = 20.dp).wrapContentSize()
                ) {
                    EmailFieldComponet(
                        modifier,
                        emailText.value,
                        onEmailInput = { emailInput ->
                        emailText.value = emailInput
                    },
                        labelText = stringResource(id = R.string.gmail),
                        leadingIconRes = R.drawable.gmail
                    )
                    PasswordFieldComponent(
                        modifier,
                        passwordText.value,
                        onPasswordInput ={ passwordInput ->
                            passwordText.value = passwordInput
                        },
                        labelText = stringResource(id = R.string.password),
                        leadingIconRes = R.drawable.lock
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    ButtonComponent(navController,modifier)
                }
            }
        }
    )
}

