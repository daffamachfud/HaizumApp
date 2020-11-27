package com.onoh.haizumapp.di

import com.onoh.haizumapp.data.AppRepository

object Injection {
    fun provideRepository(): AppRepository {
        return AppRepository.getInstance()
    }
}