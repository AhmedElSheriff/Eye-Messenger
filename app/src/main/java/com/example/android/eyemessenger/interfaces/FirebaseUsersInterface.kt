package com.example.android.eyemessenger.interfaces

import com.example.android.eyemessenger.models.User

interface FirebaseUsersInterface {
    fun onComplete(success: Boolean, users: ArrayList<User?>)

}