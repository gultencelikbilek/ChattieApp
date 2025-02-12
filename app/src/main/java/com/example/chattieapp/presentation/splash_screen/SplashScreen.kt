package com.example.chattieapp.presentation.splash_screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chattieapp.R
import com.example.chattieapp.presentation.navigation.Screen
import com.example.chattieapp.presentation.sign_in_screen.AuthState
import com.example.chattieapp.presentation.sign_in_screen.SignInViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashViewModel = hiltViewModel()
) {

    val autheCheckState = splashViewModel.authCheckState.collectAsState()

    LaunchedEffect(autheCheckState.value) {
        delay(3000)
        when (autheCheckState.value) {
            AuthCheckState.CheckAuthenticated -> {
                navController.navigate(Screen.SignUpScreen.route)
            }

            AuthCheckState.CheckUnauthenticated -> {
                navController.navigate(Screen.SignInScreen.route)
            }

            is AuthCheckState.Error -> {
                Log.d("errorpage", "${(autheCheckState.value as AuthState.Error).msg}")
            }

            AuthCheckState.Loading -> {
                Log.d("loadingState", "loading")
            }
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(25.dp)
                    )
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chat_logo),
                    contentDescription = "",
                    modifier = Modifier.clip(RoundedCornerShape(25.dp))
                )
            }
        }
    }
}