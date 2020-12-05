package com.onoh.haizumapp.vo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.onoh.haizumapp.di.Injection
import com.onoh.haizumapp.data.AppRepository
import com.onoh.haizumapp.ui.auth.AuthViewModel
import com.onoh.haizumapp.ui.chat.ChatViewModel
import com.onoh.haizumapp.ui.home.HomeViewModel

class ViewModelFactory private constructor(private val mAppRepository: AppRepository) : ViewModelProvider.NewInstanceFactory() {
    companion object{
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository())
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(mAppRepository) as T
            }
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> {
                ChatViewModel(mAppRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(mAppRepository) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}