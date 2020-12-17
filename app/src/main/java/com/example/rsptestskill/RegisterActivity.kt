package com.example.rsptestskill

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.rsptestskill.room.LoginStory
import com.example.rsptestskill.room.LoginStoryApplication
import com.example.rsptestskill.room.LoginStoryViewModel
import com.example.rsptestskill.room.LoginStoryViewModelFactory
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(){

    private val loginStoryViewModel: LoginStoryViewModel by viewModels {
        LoginStoryViewModelFactory((application as LoginStoryApplication).repository)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        onClickRegister()
    }

    private fun onClickRegister(){
        btnRegisterApply.setOnClickListener {
            var isEmpty = false
            var isRegistered = false
            val listLoginStory = ArrayList<LoginStory>()
            loginStoryViewModel.allLoginStory.observe(this) { loginStory ->
                if (loginStory.isEmpty()){
                    isEmpty = loginStory.isEmpty()
                } else {
                    loginStory.map{
                        listLoginStory.add(it)
                    }
                }
            }
            listLoginStory.map {
                if (it.email == etRegisterEmail.text.toString()){
                    isRegistered = true
                    Toast.makeText(this,"email has already register", Toast.LENGTH_LONG).show()
                }
            }
            if (!isRegistered){
                loginStoryViewModel.insert(LoginStory( null, etRegisterEmail.text.toString(), etRegisterUsername.text.toString(), 0))
            }
        }
    }

}