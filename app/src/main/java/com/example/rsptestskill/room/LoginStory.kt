package com.example.rsptestskill.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "login_story")
data class LoginStory (
    @PrimaryKey(autoGenerate = true) val id: Int?,

    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "loginCount") val loginCount: Int

)