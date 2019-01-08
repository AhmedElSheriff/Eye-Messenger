package com.example.android.eyemessenger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.android.eyemessenger.adapters.ViewPagerAdapter
import com.example.android.eyemessenger.fragments.ActiveFragment
import com.example.android.eyemessenger.fragments.MessagesFragment
import kotlinx.android.synthetic.main.activity_all_chat.*

class AllChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_chat)

        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(MessagesFragment(), "Messages")
        adapter.addFragment(ActiveFragment(), "Active")
        all_chat_view_pager.adapter = adapter
        all_chat_tab_layout.setupWithViewPager(all_chat_view_pager)




        profile_picture_settings_btn.setOnClickListener {
            val i = Intent(this,SettingsActivity::class.java)
            startActivity(i)
        }

        new_message_button.setOnClickListener {
            val i = Intent(this,SelectUserActivity::class.java)
            startActivity(i)
        }


    }
}
