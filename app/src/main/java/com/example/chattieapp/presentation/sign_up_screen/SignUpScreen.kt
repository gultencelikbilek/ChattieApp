package com.example.chattieapp.presentation.sign_up_screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chattieapp.R
import com.example.chattieapp.presentation.navigation.Screen
import com.example.chattieapp.presentation.ui_component.ButtonComponent
import com.example.chattieapp.presentation.ui_component.EmailFieldComponet
import com.example.chattieapp.presentation.ui_component.PasswordFieldComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    signUpViewModel: SignUpViewModel = hiltViewModel()
) {

    val signUpState = signUpViewModel.signUpState.collectAsState()

    var emailText by remember {
        mutableStateOf("")
    }
    var passwordText by remember {
        mutableStateOf("")
    }
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
                Box(modifier = Modifier) {
                    Image(
                        painter = painterResource(id = R.drawable.chat_logo),
                        contentDescription = null
                    )
                }
                Column() {
                    EmailFieldComponet(
                        emailText = emailText,
                        onEmailInput = {
                            emailText = it
                        },
                        labelText = stringResource(id = R.string.gmail),
                        leadingIconRes = R.drawable.gmail
                    )

                    PasswordFieldComponent(
                        passwordText = passwordText,
                        onPasswordInput ={
                                         passwordText = it
                        } ,
                        labelText = stringResource(id = R.string.password),
                        leadingIconRes = R.drawable.lock
                    )

                    ButtonComponent(
                        onClick = {
                            if (emailText.isNotEmpty() || passwordText.isNotEmpty()){
                                signUpViewModel.signUp(emailText,passwordText)
                                navController.navigate(Screen.HomePageScreen.route)
                            }else{
                                Toast.makeText(context,"Email or Password is empty", Toast.LENGTH_SHORT).show()
                            }
                        },
                        buttonText = stringResource(id = R.string.signUp)
                    )
                }
            }
        }
    )
}
