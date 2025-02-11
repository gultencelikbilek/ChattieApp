package com.example.chattieapp.presentation.navigation

sealed class Screen(val route : String) {
    data object SignInScreen : Screen("sign_in_screen")
    data object SignUpScreen : Screen("sign_up_screen")
    data object SplashScreen : Screen("spalsh_screen")
}