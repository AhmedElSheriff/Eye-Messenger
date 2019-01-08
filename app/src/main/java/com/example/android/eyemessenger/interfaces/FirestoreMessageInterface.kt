package com.example.android.eyemessenger.interfaces

import com.example.android.eyemessenger.models.Message

interface FirestoreMessageInterface {

    fun onComplete(success: Boolean)
    fun onComplete(success: Boolean, list: ArrayList<Message>)
}