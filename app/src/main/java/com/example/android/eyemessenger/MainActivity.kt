package com.example.android.eyemessenger

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.android.eyemessenger.adapters.ViewPagerAdapter
import com.example.android.eyemessenger.fragments.LoginFragment
import com.example.android.eyemessenger.fragments.SignupFragment
import com.example.android.eyemessenger.interfaces.MoveBetweenLoginAndSignup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MoveBetweenLoginAndSignup {


    override fun moveTo(dest: String) {
        if(dest == "Login"){
            view_pager.setCurrentItem(1)
        }
        else if(dest == "Signup"){
            view_pager.setCurrentItem(0)
        }
    }

    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(SignupFragment(), "Signup")
        adapter.addFragment(LoginFragment(), "Login")
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)
        tab_layout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"))
    }


    override fun onStart() {
        super.onStart()
        mAuth = FirebaseAuth.getInstance()
        if(mAuth.currentUser != null){
            val i = Intent(this, AllChatActivity::class.java)
            startActivity(i)
        }
    }
}
