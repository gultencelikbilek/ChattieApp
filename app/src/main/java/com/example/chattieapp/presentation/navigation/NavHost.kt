package com.example.chattieapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.chattieapp.presentation.home_page_screen.HomePageScreen
import com.example.chattieapp.presentation.sign_in_screen.SignInScreen
import com.example.chattieapp.presentation.sign_up_screen.SignUpScreen
import com.example.chattieapp.presentation.splash_screen.SplashScreen

@Composable
fun NavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.SplashScreen.route){
            SplashScreen(navController = navController)
        }
        composable(Screen.SignInScreen.route){
            SignInScreen(navController = navController)
        }
        composable(Screen.SignUpScreen.route){
            SignUpScreen(navController = navController)
        }
        composable(Screen.HomePageScreen.route){
            HomePageScreen(navController = navController)
        }
    }
}