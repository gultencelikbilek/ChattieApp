package com.example.chattieapp.presentation.sign_up_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.chattieapp.presentation.sign_in_screen.AuthState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.SignUpUnAuth)
    val signUpState = _signUpState.asStateFlow()

    fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _signUpState.value = SignUpState.Error("Email or password can't be empty")
        } else {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _signUpState.value = SignUpState.SignUpAuth
                    Log.d("signupviewmodel:succes","${task.isSuccessful}")

                } else {
                    _signUpState.value =
                        SignUpState.Error(task.exception?.message ?: "Something went wrong")
                    Log.d("signupviewmodel:error","${task.exception?.message}")
                }
            }
        }
    }
}

sealed class SignUpState(){
    object SignUpAuth : SignUpState()
    object SignUpUnAuth : SignUpState()
    object Loading : SignUpState()
    data class Error(val msg : String) : SignUpState()
}