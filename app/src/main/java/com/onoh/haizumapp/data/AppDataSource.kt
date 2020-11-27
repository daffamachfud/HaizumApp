package com.onoh.haizumapp.data

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface AppDataSource {

    fun authLogin(email:String,password:String): LiveData<Task<AuthResult>>
    fun authRegister(username:String,email:String,password: String) : LiveData<Task<Void>>
}