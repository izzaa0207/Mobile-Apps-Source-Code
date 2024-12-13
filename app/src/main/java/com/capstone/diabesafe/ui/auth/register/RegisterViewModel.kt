package com.capstone.diabesafe.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> get() = _registerResult

    fun registerUser(email: String, password: String) {
        _registerResult.value = Result.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val userData = mapOf(
                        "email" to email
                    )
                    database.child("users").child(userId).setValue(userData)
                        .addOnCompleteListener { dataTask ->
                            if (dataTask.isSuccessful) {
                                _registerResult.value = Result.Success("Register berhasil")
                            } else {
                                _registerResult.value = Result.Error("Gagal menyimpan data user")
                            }
                        }
                } else {
                    _registerResult.value =
                        Result.Error(task.exception?.message ?: "Register gagal")
                }
            }
    }


    sealed class Result<out T> {
        object Loading : Result<Nothing>()
        data class Success<T>(val data: T) : Result<T>()
        data class Error(val error: String) : Result<Nothing>()
    }
}
