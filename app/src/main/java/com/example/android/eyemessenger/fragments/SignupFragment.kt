package com.example.android.eyemessenger.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.eyemessenger.AllChatActivity
import com.example.android.eyemessenger.R
import com.example.android.eyemessenger.interfaces.MoveBetweenLoginAndSignup
import com.example.android.eyemessenger.models.User
import com.example.android.eyemessenger.utilities.FirebaseHelper
import com.example.android.eyemessenger.utilities.InputUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.signup_fragment.*
import kotlinx.android.synthetic.main.signup_fragment.view.*

class SignupFragment : Fragment() {

    var listener : MoveBetweenLoginAndSignup?= null

    lateinit var mAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient // Google Sign in client
    lateinit var mGoogleSignInOptions: GoogleSignInOptions // Google sign in options to switch between gmail accounts
    val RC_SIGN_IN: Int = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.signup_fragment, container, false)

        mAuth = FirebaseAuth.getInstance()
        val mFirestore = FirebaseFirestore.getInstance()
        val mUser = User()

//        if(FirebaseHelper.initFirebase().currentUser != null){
//            InputUtil.toastIt(view,"User Exist",1)
//        }
//        else{
//            InputUtil.toastIt(view,"User Not Exist",1)
//        }
        view.signup_button.setOnClickListener {


            val signupName = view.signup_name.text.toString()
            val signupEmail = view.signup_email.text.toString()
            val signupPass = view.signup_password.text.toString()

            val inputList = ArrayList<String>()
            inputList.add(signupName)
            inputList.add(signupEmail)
            inputList.add(signupPass)





            val isInputEmptyOrNull: Boolean = InputUtil.CheckInputForNullOrEmpty(inputList)
            if(!isInputEmptyOrNull){
                mUser.userName = signupName
                mUser.userEmail = signupEmail
                mUser.userPassword = signupPass
                mAuth.createUserWithEmailAndPassword(mUser.userEmail.toString(),mUser.userPassword.toString()).addOnCompleteListener {
                    if(it.isSuccessful){
                        InputUtil.toastIt(view,"Successful",1)
                        FirebaseHelper.saveUserToFirebase(mFirestore,mUser,FirebaseAuth.getInstance().currentUser)
                        val intent = Intent(this.context,AllChatActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)

                    }
                    else{
                        InputUtil.toastIt(view,it.exception.toString(),1)
                    }
                }
            }

        }

        //////////////////////////////////////// GOOGLE SIGNIN //////////////////////////////////

        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("742620538432-5qqi00kerujlov805bovet73rvb8fmg0.apps.googleusercontent.com")
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(view.context,mGoogleSignInOptions)


        view.google_signup_button.setOnClickListener {

            signInToGoogle()
            //signOutFromGoogle()
        }


        view.signup_to_login.setOnClickListener {

            if(listener == null){
                Log.d("LOG_CAT","NULL YES NULL")
            }
            listener!!.moveTo("Login")

        }
        return view;
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as MoveBetweenLoginAndSignup
    }

    private fun signInToGoogle(){
        val googleSignInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(googleSignInIntent, RC_SIGN_IN)
    }

    private fun signOutFromGoogle(){
        mAuth.signOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!
            //Toast.makeText(view?.context,"Signed in",Toast.LENGTH_LONG).show()
            firebaseAuthWithGoogle(account)
            Log.e("LOG_CAT",account.toString())
        } catch (e: ApiException){
            Toast.makeText(view?.context, e.toString(),Toast.LENGTH_LONG).show()
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken,null)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                Snackbar.make(signup_fragment,"Authentication Successful",Snackbar.LENGTH_LONG).show()
                val intent = Intent(this.context,AllChatActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            else{
                Snackbar.make(main_layout,"Authentication Failed",Snackbar.LENGTH_LONG).show()
            }
        }
    }
}