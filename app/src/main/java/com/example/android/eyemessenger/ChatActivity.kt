package com.example.android.eyemessenger

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.android.eyemessenger.interfaces.FirestoreMessageInterface
import com.example.android.eyemessenger.models.ChatMessages
import com.example.android.eyemessenger.models.Message
import com.example.android.eyemessenger.models.User
import com.example.android.eyemessenger.utilities.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val user = intent.getParcelableExtra<User>("UserKey")

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = user.userName
        val adapter = GroupAdapter<ViewHolder>()



        chat_messages_recycler_view.adapter = adapter

        send_message_button.setOnClickListener {
            val userMessage = Message()
            userMessage.userMessage = send_message_edittext.text.toString()
            userMessage.fromId = FirebaseAuth.getInstance().currentUser!!.uid
            userMessage.toId = user.uid
            userMessage.timeStamp = System.currentTimeMillis()
            send_message_edittext.text.clear()

            if(!userMessage.userMessage!!.isEmpty()){
                FirebaseHelper.saveMessageToFirestore(FirebaseFirestore.getInstance(), user, userMessage, object: FirestoreMessageInterface{

                    override fun onComplete(success: Boolean, list: ArrayList<Message>) {

                    }

                    override fun onComplete(success: Boolean) {
                        chat_messages_recycler_view.scrollToPosition(adapter.itemCount-1)
                    }

                })
            }
        }

        FirebaseHelper.getMessageFromFirestore(FirebaseFirestore.getInstance(),user,object: FirestoreMessageInterface{
            override fun onComplete(success: Boolean) {

            }

            override fun onComplete(success: Boolean, list: ArrayList<Message>) {
                if (success){
                    chat_messages_recycler_view.scrollToPosition(list.size -1)
                    list.forEach {
                        if(it.fromId == user.uid){
                            adapter.add(ChatMessages.Companion.ChatFrom(it.userMessage!!))
                        }
                        else{
                            adapter.add(ChatMessages.Companion.ChatTo(it.userMessage!!))
                        }
                    }
                }
            }

        })

    }
}
