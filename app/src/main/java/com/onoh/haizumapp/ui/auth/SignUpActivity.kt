package com.onoh.haizumapp.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.onoh.haizumapp.R
import com.onoh.haizumapp.ui.MainActivity
import com.onoh.haizumapp.vo.ViewModelFactory
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //inisiasi firebase auth
        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this, factory)[AuthViewModel::class.java]

        btn_register_submit.setOnClickListener {
            val userName = et_register_fullname.text.toString()
            val email = et_register_email.text.toString()
            val password = et_register_password.text.toString()
            val confirmPass = et_register_confirm_password.text.toString()
            if (userName.isEmpty()) {
                et_register_fullname.error = "Nama tidak valid"
            }
            if (email.isEmpty()) {
                et_register_email.error = "Email tidak valid"
            }
            if (password.isEmpty()) {
                et_register_password.error = "Password tidak valid"
            }
            if (confirmPass.isEmpty()) {
                et_register_fullname.error = "Isi kembali password"
            }
            if (password != confirmPass) {
                Toast.makeText(this, "Konfirmasi password kamu salah!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.setRegister(userName, email, password)
                registerUser(viewModel.authRegister())
            }
        }

        tv_login_here.setOnClickListener{
            val intentLogin = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intentLogin)
            finish()
        }
    }

    private fun registerUser(authRegister: LiveData<Task<Void>>) {
        authRegister.observe(this,{
            if(it.isSuccessful){
                val intentUser = Intent(this@SignUpActivity, MainActivity::class.java)
                startActivity(intentUser)
                finish()
            }else{
                Toast.makeText(this,"Terjadi kesalahan, coba lagi !",Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

}