package com.example.android.eyemessenger.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.eyemessenger.ChatActivity
import com.example.android.eyemessenger.R
import com.example.android.eyemessenger.interfaces.FirebaseUserInterface3
import com.example.android.eyemessenger.interfaces.FirebaseUsersInterface2
import com.example.android.eyemessenger.models.ChatMessages
import com.example.android.eyemessenger.models.Message
import com.example.android.eyemessenger.models.User
import com.example.android.eyemessenger.utilities.FirebaseHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.messages_fragment.view.*


class MessagesFragment: Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.messages_fragment, container, false)

        val adapter = GroupAdapter<ViewHolder>()
        val list = ArrayList<User>()
        val hashMap = HashMap<String,Message>()
                FirebaseHelper.getLastMessage(FirebaseFirestore.getInstance(),object: FirebaseUsersInterface2 {

                    override fun onComplete2(success: Boolean, messages: HashMap<String,Message>) {
                        adapter.clear()



                        messages.forEach {
                            hashMap.put(it.key,it.value)

                            adapter.add(ChatMessages.Companion.LatestChats(it.value,object: FirebaseUserInterface3{
                                override fun onComplete(success: Boolean, user: User) {
                                    list.add(user)
                                }

                            }))

                        }
//                          for(message: Message in messages){
//
//
//                              adapter.add(ChatMessages.Companion.LatestChats(message, object: FirebaseUserInterface3{
//                                  override fun onComplete(success: Boolean, user: User) {
//
//                                      list.add(user)
//                                  }
//
//                              }))
//                        }
                        adapter.notifyDataSetChanged()


                    }



                })



        view.messages_recycler_view2.adapter = adapter


        adapter.setOnItemClickListener { item, view  ->


            val intent = Intent(this.context, ChatActivity::class.java)
            intent.putExtra("UserKey", list.get(adapter.getAdapterPosition(item)))
            //val raw = item as ChatMessages.Companion.LatestChats
            //intent.putExtra("UserKey", raw.message)
            startActivity(intent)
        }



        return view
    }
}