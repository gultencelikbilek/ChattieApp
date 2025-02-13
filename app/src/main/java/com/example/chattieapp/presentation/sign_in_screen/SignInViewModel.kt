package com.example.chattieapp.presentation.sign_in_screen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(): ViewModel() {

    // Firebase Authentication servisine erişmek için bir nesne oluşturuldu
    //kullanıcı girişi yapmak, çıkış yapmak veya giriş durumunu kontrol etmek gibi işlemler için kullanılabilir.
    private val auth: FirebaseAuth =
        FirebaseAuth.getInstance()//auth kullanıcı kimlik doğrulama için kullanılabilir.
    //auth ile kullanıcı giriş yapabilir, çıkış yapabilir oturum durumunu kotnrol edebilir.

    //mutablestateflow başlangıç değeri alması gerekir şuan giriş yapılmadığı için unautheticeted verildi
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()


    fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value =
                        AuthState.Error(it.exception?.message ?: "Something went wrong")
                }
            }
    }



    fun signOut(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val msg: String) : AuthState()

}