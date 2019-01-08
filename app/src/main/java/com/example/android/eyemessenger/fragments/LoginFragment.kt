package com.example.android.eyemessenger.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.eyemessenger.AllChatActivity
import com.example.android.eyemessenger.R
import com.example.android.eyemessenger.interfaces.MoveBetweenLoginAndSignup
import com.example.android.eyemessenger.utilities.InputUtil
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.login_fragment.view.*

class LoginFragment : Fragment(){

    var listener : MoveBetweenLoginAndSignup?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.login_fragment, container, false);
        val mAuth = FirebaseAuth.getInstance()
        view.login_to_signup.setOnClickListener {
            if(listener == null){
                Log.d("LOG_CAT","NULL YES NULL")
            }
            listener!!.moveTo("Signup")
        }

        view.login_button.setOnClickListener {
            val mUserEmail: String = login_email.text.toString()
            val mUserPassword: String = login_password.text.toString()

            val inputList = ArrayList<String>()
            inputList.add(mUserEmail)
            inputList.add(mUserPassword)

            val isInputEmptyOrNull: Boolean = InputUtil.CheckInputForNullOrEmpty(inputList)
            InputUtil.toastIt(view,isInputEmptyOrNull.toString(),1)
            if(!isInputEmptyOrNull){
                mAuth.signInWithEmailAndPassword(mUserEmail,mUserPassword).addOnCompleteListener {
                    if(it.isSuccessful){
                        InputUtil.toastIt(view,"Successful",1)
                        val intent = Intent(this.context, AllChatActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                    else{
                        InputUtil.toastIt(view,it.exception.toString(),1)
                    }
                }
            }

        }
        return view;
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as MoveBetweenLoginAndSignup
    }
}