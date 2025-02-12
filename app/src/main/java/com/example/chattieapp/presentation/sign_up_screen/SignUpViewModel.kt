package com.example.chattieapp.presentation.sign_up_screen

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.SignUpUnAuth)
    val
}

sealed class SignUpState(){
    object SignUpAuth : SignUpState()
    object SignUpUnAuth : SignUpState()
    object Loading : SignUpState()
    data class Error(val msg : String) : SignUpState()
}