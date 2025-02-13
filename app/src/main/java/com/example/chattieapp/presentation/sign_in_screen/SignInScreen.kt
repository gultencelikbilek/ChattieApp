package com.example.chattieapp.presentation.sign_in_screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chattieapp.R
import com.example.chattieapp.presentation.navigation.Screen
import com.example.chattieapp.presentation.ui_component.ButtonComponent
import com.example.chattieapp.presentation.ui_component.EmailFieldComponet
import com.example.chattieapp.presentation.ui_component.PasswordFieldComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    signInViewModel: SignInViewModel = hiltViewModel()
) {

    var emailText = remember {
        mutableStateOf("")
    }
    var passwordText = remember {
        mutableStateOf("")
    }
    val authState = signInViewModel.authState.collectAsState()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.app_color)
                )
            )
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
                    modifier = Modifier
                        .wrapContentSize()
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
                        onPasswordInput = { passwordInput ->
                            passwordText.value = passwordInput
                        },
                        labelText = stringResource(id = R.string.password),
                        leadingIconRes = R.drawable.lock
                    )
                }
                Box(
                    modifier = Modifier
                        .wrapContentSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    ButtonComponent(
                        modifier, onClick = {
                            if (emailText.value.isNotEmpty() || passwordText.value.isNotEmpty()){
                                signInViewModel.signIn(emailText.value, passwordText.value)
                                navController.navigate(Screen.HomePageScreen.route)
                            }else{
                                Toast.makeText(context,"Email or Password is empty",Toast.LENGTH_SHORT).show()
                            }

                        },
                        buttonText = stringResource(id = R.string.login)
                    )
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                            //.padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            onClick = {
                                navController.navigate(Screen.SignUpScreen.route)
                            }
                        ) {
                            Text(
                                text = stringResource(id = R.string.go_to_signup_page),
                                style = TextStyle(
                                    color = colorResource(id = R.color.gray)
                                )
                            )
                        }
                    }
                }

            }
        }
    )
}

