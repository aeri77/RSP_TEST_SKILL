package com.example.rsptestskill.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LoginStoryDao {
    @Query("SELECT * FROM login_story ORDER BY id ASC")
    fun getIdLoginStory(): Flow<List<LoginStory>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg loginStory: LoginStory)

    @Update()
    suspend fun update(vararg loginStory: LoginStory)

    @Query("DELETE FROM login_story")
    suspend fun deleteAll()
}