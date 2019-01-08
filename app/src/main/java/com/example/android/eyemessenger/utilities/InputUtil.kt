package com.example.android.eyemessenger.utilities

import android.view.View
import android.widget.Toast

class InputUtil{
    companion object {
        fun CheckInputForNullOrEmpty(inputList: ArrayList<String>): Boolean{
            var isEmptyRrNull = false
            for(i in inputList){
                if(i.isEmpty()){
                    isEmptyRrNull = true
                    return true
                }
            }

            return isEmptyRrNull
        }

        fun toastIt(view: View, input: String ,length: Int){
            Toast.makeText(view.context,input,length).show()
        }
    }
}