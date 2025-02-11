package com.example.chattieapp.presentation.splash_screen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chattieapp.R
import com.example.chattieapp.presentation.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {


    LaunchedEffect(Unit) {
        delay(1000)
        navController.navigate(Screen.SignInScreen.route)
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