package com.onoh.haizumapp.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.onoh.haizumapp.R
import com.onoh.haizumapp.ui.MainActivity
import com.onoh.haizumapp.vo.ViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this,factory)[AuthViewModel::class.java]

        btn_login_submit.setOnClickListener{
            val email = et_login_email.text.toString()
            val password = et_login_password.text.toString()

            if(email.isEmpty() && password.isEmpty()){
                Toast.makeText(this,"Email dan password harap diisi ! ",Toast.LENGTH_SHORT).show()
            }
            else{
                viewModel.setLogin(email,password)
                resultLogin(viewModel.authLogin())
            }

        }

        tv_signup_here.setOnClickListener{
            val intentRegister = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intentRegister)
            finish()
        }
    }

    private fun resultLogin(authLogin: LiveData<Task<AuthResult>>) {
        authLogin.observe(this,{
            if(it.isSuccessful){
                val intentLogin = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intentLogin)
            }else{
                Toast.makeText(this,"Email atau password salah !",Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null){
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
