package com.example.rsptestskill.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [LoginStory::class], version = 1, exportSchema = false)
public abstract class LoginStoryRoomDb : RoomDatabase() {

    abstract fun loginStoryDao(): LoginStoryDao

    private class LoginStoryCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.loginStoryDao())
                }
            }
        }

        suspend fun populateDatabase(loginStoryDao: LoginStoryDao) {
//            // Delete all content here.
////            loginStoryDao.deleteAll()
//
//            // Add sample words.
//            var word = LoginStory( 0 , "test@mail.com", 1)
//            loginStoryDao.insert(word)
//            word = LoginStory(1 , "t44t@mail.com", 2)
//            loginStoryDao.insert(word)
//
//            // TODO: Add your own words!
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: LoginStoryRoomDb? = null

        fun getDatabase(context: Context,
                        scope: CoroutineScope
        ): LoginStoryRoomDb {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LoginStoryRoomDb::class.java,
                    "login_story_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}