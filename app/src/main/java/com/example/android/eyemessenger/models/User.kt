package com.example.android.eyemessenger.models

import android.os.Parcel
import android.os.Parcelable

class User() : Parcelable{

    var userName: String? = null
    var userEmail: String? = null
    var userPassword: String? = null
    var profilePicture: String? = null
    var uid: String? = null

    constructor(parcel: Parcel) : this() {
        userName = parcel.readString()
        userEmail = parcel.readString()
        userPassword = parcel.readString()
        profilePicture = parcel.readString()
        uid = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userName)
        parcel.writeString(userEmail)
        parcel.writeString(userPassword)
        parcel.writeString(profilePicture)
        parcel.writeString(uid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}