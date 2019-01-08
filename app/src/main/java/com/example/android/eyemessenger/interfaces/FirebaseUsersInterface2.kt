package com.example.android.eyemessenger.interfaces

import com.example.android.eyemessenger.models.Message

interface FirebaseUsersInterface2 {

    //fun onComplete2(success: Boolean, messages: ArrayList<Message>)
    fun onComplete2(success: Boolean, messages: HashMap<String,Message>)
}