package com.onoh.haizumapp.data

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.onoh.haizumapp.data.model.Chat
import com.onoh.haizumapp.data.model.Post
import com.onoh.haizumapp.data.model.User

interface AppDataSource {

    fun authLogin(email:String,password:String): LiveData<Task<AuthResult>>
    fun authRegister(username:String,email:String,password: String) : LiveData<Task<Void>>
    fun getUser(userId:String):LiveData<User>
    fun getPost():LiveData<List<Post>>
    fun post(userId:String,time:String,text:String,imgUrl:String,videoUrl:String)
    fun sendMessageChat(senderId:String,date:String,message:String)
    fun readMessageChat(senderId:String):LiveData<List<Chat>>
}