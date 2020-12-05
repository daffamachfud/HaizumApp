package com.onoh.haizumapp.ui.home.post

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.onoh.haizumapp.R
import com.onoh.haizumapp.ui.chat.ChatViewModel
import com.onoh.haizumapp.ui.home.HomeViewModel
import com.onoh.haizumapp.vo.ViewModelFactory
import kotlinx.android.synthetic.main.activity_post.*
import kotlinx.android.synthetic.main.fragment_chat.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class PostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this,factory)[HomeViewModel::class.java]

        val firebaseUser = FirebaseAuth.getInstance().currentUser

        //set button close
        btn_close_post.setOnClickListener{
            onBackPressed()
        }

        btn_post.setOnClickListener{
            val text = et_post.text.toString()
           //get time
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val currentDateTime = LocalDateTime.now()
                val time = currentDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                viewModel.setPost(firebaseUser!!.uid,time,text,"","")
                viewModel.sendPost()
                finish()
            }else{
                val date = Date()
                val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
                val answer: String = formatter.format(date)
                viewModel.setPost(firebaseUser!!.uid,answer,text,"","")
                viewModel.sendPost()
                finish()
            }
        }
    }
}
