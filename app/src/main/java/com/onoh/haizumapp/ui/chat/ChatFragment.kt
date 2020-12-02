package com.onoh.haizumapp.ui.chat

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.onoh.haizumapp.R
import com.onoh.haizumapp.data.model.Chat
import com.onoh.haizumapp.vo.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_chat.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class ChatFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val factory = ViewModelFactory.getInstance()
        val viewModel = ViewModelProvider(this,factory)[ChatViewModel::class.java]

        val firebaseUser = FirebaseAuth.getInstance().currentUser

        et_chat_message.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            @SuppressLint("SimpleDateFormat")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.isEmpty()){
                    text_layout_chat.endIconDrawable = null
                    text_layout_chat.isFocusable = false
                    text_layout_chat.isClickable = false
                }else{
                    text_layout_chat.endIconDrawable = resources.getDrawable(R.drawable.ic_send_white)
                    text_layout_chat.setEndIconOnClickListener {
                        val message = et_chat_message.text.toString()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val currentDateTime = LocalDateTime.now()
                            val date = currentDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
                            viewModel.setSendChat(firebaseUser!!.uid,message,date)
                            viewModel.sendChat()
                            et_chat_message.setText("")
                        }else{
                            val date = Date()
                            val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
                            val answer: String = formatter.format(date)
                            viewModel.setSendChat(firebaseUser!!.uid,message,answer)
                            viewModel.sendChat()
                            et_chat_message.setText("")
                        }

                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        viewModel.setMessage(firebaseUser!!.uid)
        loading_chat.visibility = View.VISIBLE
        setMessage(viewModel.getChat())
    }

    private fun setMessage(chat: LiveData<List<Chat>>) {
        val chatAdapter = ChatAdapter()
        chat.observe(viewLifecycleOwner,{
            chatAdapter.setChat(it)
            loading_chat.visibility = View.GONE
            chatAdapter.notifyDataSetChanged()
            rv_chat.smoothScrollToPosition(chatAdapter.itemCount)
        })
        with(rv_chat){
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = chatAdapter
        }
    }
}
