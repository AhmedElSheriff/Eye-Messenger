package com.example.android.eyemessenger.models

import com.example.android.eyemessenger.R
import com.example.android.eyemessenger.interfaces.FirebaseUserInterface3
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.single_chat_item.view.*
import kotlinx.android.synthetic.main.single_message_reciever.view.*
import kotlinx.android.synthetic.main.single_message_sender.view.*

class ChatMessages{

    companion object {
        class ChatFrom(val message: String): Item<ViewHolder>(){
            override fun getLayout(): Int {
                return R.layout.single_message_reciever
            }

            override fun bind(viewHolder: ViewHolder, position: Int) {
                viewHolder.itemView.message_from_textview.text = message
            }

        }


        class ChatTo(val message: String): Item<ViewHolder>(){
            override fun getLayout(): Int {
                return R.layout.single_message_sender
            }

            override fun bind(viewHolder: ViewHolder, position: Int) {
                viewHolder.itemView.message_to_textview.text = message
            }

        }

        class LatestChats(val message: Message, val userInterface: FirebaseUserInterface3): Item<ViewHolder>(){
            override fun getLayout(): Int {
                return R.layout.single_chat_item

            }


            override fun bind(viewHolder: ViewHolder, position: Int) {

                val userId: String
                var userName: String ?= null
                var user: User ?= null
                if(FirebaseAuth.getInstance().currentUser!!.uid == message.fromId){
                    userId = message.toId!!
                }
                else{
                    userId = message.fromId.toString()
                }

                FirebaseFirestore.getInstance().collection("Users").document(userId)
                        .addSnapshotListener(MetadataChanges.INCLUDE){
                            snapshot, e->


                            user = snapshot!!.toObject(User::class.java)
                            viewHolder.itemView.singlechat_name.text = user!!.userName
                            viewHolder.itemView.singlechat_message.text = message.userMessage

                            if(user != null){
                                userInterface.onComplete(true, user!!)
                            }


                        }




            }

        }
    }

}
