package com.onoh.haizumapp.ui.chat


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.onoh.haizumapp.R
import com.onoh.haizumapp.data.model.Chat
import kotlinx.android.synthetic.main.item_sender_chat.view.*

class ChatAdapter:RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    companion object{
        const val MESSAGE_TYPE_RECEIVER = 0
        const val MESSAGE_TYPE_SENDER = 1
    }

    var firebaseUser:FirebaseUser? = null

    private var listChat = ArrayList<Chat?>()

    fun setChat(chats: List<Chat?>){
        listChat.clear()
        listChat.addAll(chats)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return if(viewType == MESSAGE_TYPE_SENDER){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sender_chat, parent, false)
            ChatViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_receiver_chat, parent, false)
            ChatViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = listChat[position]
        if (chat != null) {
            holder.bind(chat)
        }
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return if(listChat[position]?.senderId == firebaseUser!!.uid){
            MESSAGE_TYPE_SENDER
        }else{
            MESSAGE_TYPE_RECEIVER
        }
    }

    override fun getItemCount(): Int = listChat.size

    class ChatViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bind(chat: Chat){
            with(itemView) {
                tv_message_chat_item.text = chat.message.toString()
                tv_date_chat_item.text = chat.date.toString()
                tv_username_chat_item.text = chat.username.toString()
            }
        }
    }


}