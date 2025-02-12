package com.example.chattieapp.presentation.splash_screen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel  @Inject constructor() : ViewModel(){

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _autheCheckState = MutableStateFlow<AuthCheckState>(AuthCheckState.CheckUnauthenticated)
    val authCheckState = _autheCheckState.asStateFlow()
    init {
        checkAuthStatus()
    }
    fun checkAuthStatus() { //kullanıcının giriş yapıp yapmadığını kontrol etmek için kullanılır.
        if (auth.currentUser == null) { // Firebase Authentication servisi üzerinden şu an giriş yapmış olan kullanıcıyı döndürür.
            _autheCheckState.value = AuthCheckState.CheckUnauthenticated
        } else {
            _autheCheckState.value = AuthCheckState.CheckAuthenticated
        }
    }
}

sealed class AuthCheckState{
    object CheckAuthenticated : AuthCheckState()
    object CheckUnauthenticated : AuthCheckState()
    object Loading : AuthCheckState()
    data class Error(val msg : String) : AuthCheckState()
}