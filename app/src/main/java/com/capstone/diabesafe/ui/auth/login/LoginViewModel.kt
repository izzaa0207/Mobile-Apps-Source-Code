package com.capstone.diabesafe.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> get() = _loginResult

    fun loginUser(email: String, password: String) {
        _loginResult.value = LoginResult.Loading

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginResult.value = LoginResult.Success("Login berhasil")
                } else {
                    _loginResult.value = LoginResult.Error(task.exception?.message ?: "Login gagal")
                }
            }
    }

    sealed class LoginResult {
        object Loading : LoginResult()
        data class Success(val message: String) : LoginResult()
        data class Error(val error: String) : LoginResult()
    }
}
