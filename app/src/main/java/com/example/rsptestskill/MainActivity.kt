package com.example.rsptestskill

import android.content.ContentValues
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

private const val TAG = "MainActivity"

class   MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val database = baseContext.openOrCreateDatabase("login_story", Context.MODE_PRIVATE, null)
        var sql = "CREATE TABLE IF NOT EXISTS login_story(_id INTEGER PRIMARY KEY NOT NULL, email TEXT, count_login INTEGER)"
        Log.d(TAG, "onCreate: sql is $sql")
        database.execSQL(sql)

        sql = "INSERT INTO login_story(email, count_login) VALUES('tim@hisemail.com', 1)"
        Log.d(TAG, "onCreate: sql is $sql")
        database.execSQL(sql)

        val values = ContentValues().apply {
            put("email", "fred@nurk.com")
            put("count_login", 12)
        }

        //         val values = ContentValues()
        //         values.put("name", "Fred")
        //         values.put("phone", 12345)
        //         values.put("email", "fred@nurk.com")

        val generatedId = database.insert("login_story",  null, values)
        Log.d(TAG, "onCreate: record added with id $generatedId")

    }
}