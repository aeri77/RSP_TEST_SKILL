package com.example.rsptestskill.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginStoryViewModelFactory(private val repository: LoginStoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginStoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginStoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}