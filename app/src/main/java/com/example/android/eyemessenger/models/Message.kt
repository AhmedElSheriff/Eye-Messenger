package com.example.android.eyemessenger.models

import android.os.Parcel
import android.os.Parcelable

class Message(): Parcelable {
    var userMessage: String? = null
    var timeStamp: Long? = null
    var fromId: String? = null
    var toId: String? = null

    constructor(parcel: Parcel) : this() {
        userMessage = parcel.readString()
        timeStamp = parcel.readValue(Long::class.java.classLoader) as? Long
        fromId = parcel.readString()
        toId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userMessage)
        parcel.writeValue(timeStamp)
        parcel.writeString(fromId)
        parcel.writeString(toId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }

}