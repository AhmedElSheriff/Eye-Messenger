package com.example.android.eyemessenger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.android.eyemessenger.interfaces.FirebaseUsersInterface
import com.example.android.eyemessenger.models.SelectUserItems
import com.example.android.eyemessenger.models.User
import com.example.android.eyemessenger.utilities.FirebaseHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_select_user.*

class SelectUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)

        val mFirebaseFireStore = FirebaseFirestore.getInstance()
        setSupportActionBar(toolbar_selectuser)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val adapter = GroupAdapter<ViewHolder>()

        var list = ArrayList<User?>()
//        FirebaseHelper.getAllUsersFromFirebase(mFirebaseFireStore, object: FirebaseUsersInterface{
//            override fun onComplete(success: Boolean, users: ArrayList<User?>) {
//                if(success){
//                    list = users
//                    Log.d("LOG_CAT",users.toString())
//
//                    for (user: User? in list){
//                        adapter.add(SelectUserItems(user!!))
//                    }
//                }
//            }
//
//        })

        FirebaseHelper.getAllUsersFromFirestore(mFirebaseFireStore, object: FirebaseUsersInterface{

            override fun onComplete(success: Boolean, users: ArrayList<User?>) {
                if(success){
                    list = users
                    Log.d("LOG_CAT",users.toString())

                    for (user: User? in list){
                        adapter.add(SelectUserItems(user!!))
                    }
                }
            }

        })




        select_user_recycler_view.adapter = adapter
        adapter.setOnItemClickListener { item, _ ->
            val userItem = item as SelectUserItems
            val intent = Intent(this,ChatActivity::class.java)
            intent.putExtra("UserKey",userItem.user)
            startActivity(intent)

        }

    }
}
