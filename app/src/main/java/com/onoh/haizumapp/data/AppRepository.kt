package com.onoh.haizumapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

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


}