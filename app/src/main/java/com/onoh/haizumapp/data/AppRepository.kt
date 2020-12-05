package com.onoh.haizumapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.onoh.haizumapp.data.model.Chat
import com.onoh.haizumapp.data.model.Post
import com.onoh.haizumapp.data.model.User

class AppRepository  :AppDataSource{

    companion object{
        @Volatile
        private var instance:AppRepository?=null
        fun getInstance(): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository()
            }
    }


    private var auth:FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var databaseReference: DatabaseReference
    val charResults = MutableLiveData<List<Chat>>()
    val postResults = MutableLiveData<List<Post>>()

    override fun authLogin(email: String, password: String): LiveData<Task<AuthResult>> {
        val result = MutableLiveData<Task<AuthResult>>()
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
            result.value = it
        }
        return result
    }

    override fun authRegister(username: String, email: String, password: String): LiveData<Task<Void>> {
        val result = MutableLiveData<Task<Void>>()
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    val user:FirebaseUser? = auth.currentUser
                    val userId : String? = user?.uid

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap["userId"] = userId
                    hashMap["username"] = username
                    hashMap["profileImage"] = ""

                    databaseReference.setValue(hashMap).addOnCompleteListener{database->
                        result.value = database
                    }
                }
            }
        return result
    }

    override fun getUser(userId: String): LiveData<User> {
        val result = MutableLiveData<User>()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                result.value = user
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("onResponse", error.message)
            }
        })
        return result
    }

    override fun getPost(): LiveData<List<Post>> {
        val resultPost = ArrayList<Post>()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Post")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                resultPost.clear()
                for (dataSnapshot: DataSnapshot in snapshot.children) {
                    val post = dataSnapshot.getValue(Post::class.java)
                    if (post != null) {
                        resultPost.add(post)
                    }
                }
                postResults.postValue(resultPost)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("onResponse", error.message)
            }
        })
        return postResults
    }

    override fun post(userId: String, time: String, text: String, imgUrl: String, videoUrl: String) {
       val reference = FirebaseDatabase.getInstance().reference
        val userReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        userReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result =snapshot.getValue(User::class.java)
                val username = result?.username
                val profileImg = result?.profileImage
                val hashMap:HashMap<String,String> = HashMap()
                val viewType: Int = if(imgUrl.isEmpty() || videoUrl.isEmpty()){
                    1
                }else if (videoUrl.isEmpty()){
                    2
                }else{
                    3
                }

                hashMap["userId"] = userId
                hashMap["photoProfile"] = profileImg.toString()
                hashMap["name"] = username.toString()
                hashMap["time"] = time
                hashMap["viewType"] = viewType.toString()
                hashMap["text"] = text
                hashMap["imgUrl"] = imgUrl
                hashMap["videoUrl"] = videoUrl
                reference.child("Post").push().setValue(hashMap)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("onResponse", error.message)
            }
        })
    }


    override fun sendMessageChat(senderId: String, date: String, message: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val userReference = FirebaseDatabase.getInstance().getReference("Users").child(senderId)

        //get username

        userReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result =snapshot.getValue(User::class.java)
                val username = result?.username
                val hashMap:HashMap<String,String> = HashMap()
                hashMap["senderId"] = senderId
                hashMap["username"] = username.toString()
                hashMap["date"] = date
                hashMap["message"] = message
                reference.child("Chat").push().setValue(hashMap)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("onResponse", error.message)
            }
        })

    }

    override fun readMessageChat(senderId: String): LiveData<List<Chat>> {
        val resultChat = ArrayList<Chat>()
        databaseReference = FirebaseDatabase.getInstance().getReference("Chat")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                resultChat.clear()
                for(dataSnapshot in snapshot.children){
                    val chat = dataSnapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        resultChat.add(chat)
                    }
                }
                charResults.postValue(resultChat)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("onResponse", error.message)
            }
        })
        return charResults
    }


}