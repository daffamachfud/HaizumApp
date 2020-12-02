package com.onoh.haizumapp.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onoh.haizumapp.data.AppRepository
import com.onoh.haizumapp.data.model.Chat

class ChatViewModel(private val appRepository: AppRepository) : ViewModel() {
    private val senderId = MutableLiveData<String>()
    private val message = MutableLiveData<String>()
    private val date = MutableLiveData<String>()

    fun setSendChat(senderId:String,message:String,date:String){
        this.senderId.value = senderId
        this.message.value = message
        this.date.value = date
    }

    fun setMessage(senderId: String){
        this.senderId.value = senderId
    }

    fun sendChat() = appRepository.sendMessageChat(senderId.value!!,date.value!!,message.value!!)
    fun getChat():LiveData<List<Chat>> = appRepository.readMessageChat(senderId.value!!)
}