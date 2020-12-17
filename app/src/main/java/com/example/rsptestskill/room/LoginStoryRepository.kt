package com.example.rsptestskill.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class LoginStoryRepository(private val loginStoryDao: LoginStoryDao) {
    val allLoginStory: Flow<List<LoginStory>> = loginStoryDao.getIdLoginStory()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(loginStory: LoginStory){
        loginStoryDao.insert(loginStory)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(){
        loginStoryDao.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(loginStory: LoginStory){
        loginStoryDao.update(loginStory)
    }
}