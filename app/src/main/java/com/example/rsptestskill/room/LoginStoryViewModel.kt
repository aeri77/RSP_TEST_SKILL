package com.example.rsptestskill.room

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class LoginStoryViewModel(private val repository: LoginStoryRepository) : ViewModel() {

    val allLoginStory: LiveData<List<LoginStory>> = repository.allLoginStory.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(loginStory: LoginStory) = viewModelScope.launch {
        repository.insert(loginStory)
    }

    fun delete() = viewModelScope.launch {
        repository.delete()
    }

    fun update(loginStory: LoginStory) = viewModelScope.launch {
        repository.update(loginStory)
    }
}
