package com.onoh.haizumapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.onoh.haizumapp.data.AppRepository
import com.onoh.haizumapp.data.model.Post
import com.onoh.haizumapp.data.model.User

class HomeViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val userId =MutableLiveData<String>()
    private val time =MutableLiveData<String>()
    private val text =MutableLiveData<String>()
    private val imgUrl =MutableLiveData<String>()
    private val videoUrl =MutableLiveData<String>()


    fun setUser(userId:String?){
        this.userId.value = userId
    }

    fun setPost(userId: String?,time:String?,text:String?,imgUrl:String?,videoUrl:String?){
        this.userId.value = userId
        this.time.value = time
        this.text.value = text
        this.imgUrl.value = imgUrl
        this.videoUrl.value = videoUrl
    }

    fun getUser():LiveData<User> = Transformations.switchMap(userId){
        appRepository.getUser(it)
    }

    fun sendPost() = appRepository.post(userId.value!!,time.value!!,text.value!!,imgUrl.value!!,videoUrl.value!!)

    fun getPost():LiveData<List<Post>> = appRepository.getPost()
}