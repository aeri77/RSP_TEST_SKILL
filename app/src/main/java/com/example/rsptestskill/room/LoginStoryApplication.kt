package com.example.rsptestskill.room

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class LoginStoryApplication: Application(){
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { LoginStoryRoomDb.getDatabase(this, applicationScope) }
    val repository by lazy { LoginStoryRepository(database.loginStoryDao()) }
}
