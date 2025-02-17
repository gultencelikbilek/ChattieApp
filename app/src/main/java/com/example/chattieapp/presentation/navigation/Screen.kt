package com.example.chattieapp.presentation.navigation

sealed class Screen(val route : String) {
    data object SignInScreen : Screen("sign_in_screen")
    data object SignUpScreen : Screen("sign_up_screen")
    data object SplashScreen : Screen("splash_screen")
    data object HomePageScreen : Screen("home_page_screen")
    data object ChatScreen : Screen("chat_screen")
}