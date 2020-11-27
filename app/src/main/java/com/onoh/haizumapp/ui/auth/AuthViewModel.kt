package com.onoh.haizumapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.onoh.haizumapp.data.AppRepository

class AuthViewModel(private val appRepository: AppRepository):ViewModel() {
    private val email = MutableLiveData<String>()
    private val password = MutableLiveData<String>()
    private val username = MutableLiveData<String>()

    fun setLogin(email:String,password:String){
        this.email.value = email
        this.password.value = password
    }

    fun setRegister(username:String,email:String,password: String){
        this.username.value = username
        this.password.value = password
        this.email.value = email
    }

    fun authLogin(): LiveData<Task<AuthResult>> = appRepository.authLogin(email.value!!, password.value!!)

    fun authRegister(): LiveData<Task<Void>> = appRepository.authRegister(username.value!!,email.value!!,password.value!!)
}