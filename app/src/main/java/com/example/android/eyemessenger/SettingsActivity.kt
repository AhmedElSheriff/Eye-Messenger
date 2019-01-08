package com.example.android.eyemessenger

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.android.eyemessenger.interfaces.FirebaseInterfaces
import com.example.android.eyemessenger.utilities.FirebaseHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*

class SettingsActivity : AppCompatActivity(){



    val RC_CODE = 0
    lateinit var mAuth: FirebaseAuth
    lateinit var mFirebaseFirestore: FirebaseFirestore
    lateinit var mFirebaseStorage: FirebaseStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar_settings)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        mAuth = FirebaseAuth.getInstance()
        mFirebaseFirestore = FirebaseFirestore.getInstance()
        mFirebaseStorage = FirebaseStorage.getInstance()

        FirebaseHelper.returnImageFromFirebaseStorage(mFirebaseStorage,mAuth.currentUser!!.email.toString(), object: FirebaseInterfaces{
            override fun isSuccessful(result: Boolean, uri: Uri) {
                Toast.makeText(baseContext,"Hmmmm",Toast.LENGTH_LONG).show()
                Picasso.get().load(uri).into(profile_picture_settings_btn)
            }

        })


        val listItems = ArrayList<String>()
        listItems.add("Sign Out")

        val arrayAdapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems)
        settings_list_view.adapter = arrayAdapter
        
        settings_list_view.setOnItemClickListener { adapterView, view, pos, _ ->
            if(pos == 0){
                mAuth.signOut()
                val intent = Intent(this,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        profile_picture_settings_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"


            startActivityForResult(intent, RC_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_CODE && data!= null){
            val mFirebaseStorage = FirebaseStorage.getInstance()
            val uri = data!!.data
            FirebaseHelper.uploadImageIntoFirebaseStorage(uri,mFirebaseStorage, mAuth.currentUser!!.email.toString(), mFirebaseFirestore)
        }

    }

}
