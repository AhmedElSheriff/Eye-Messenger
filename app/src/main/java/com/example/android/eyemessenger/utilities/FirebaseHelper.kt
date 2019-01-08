package com.example.android.eyemessenger.utilities

import android.net.Uri
import android.util.Log
import com.example.android.eyemessenger.interfaces.FirebaseInterfaces
import com.example.android.eyemessenger.interfaces.FirebaseUsersInterface
import com.example.android.eyemessenger.interfaces.FirebaseUsersInterface2
import com.example.android.eyemessenger.interfaces.FirestoreMessageInterface
import com.example.android.eyemessenger.models.Message
import com.example.android.eyemessenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FirebaseHelper{

    companion object {
//        fun initFirebase(context: Context): FirebaseAuth{
//            FirebaseApp.initializeApp(context)
//            return FirebaseAuth.getInstance()
//        }

        fun uploadImageIntoFirebaseStorage(uri: Uri, storage: FirebaseStorage, email: String, db: FirebaseFirestore){

            val fileName = "profilePicture"
            val ref= storage.getReference("/images/test.test/$fileName")
            ref.putFile(uri).addOnCompleteListener {
                if(it.isSuccessful){
                    ref.downloadUrl.addOnSuccessListener {

                        val hashMap = HashMap<String,String>()
                        hashMap.put("profilePicture",it.toString())
                        addInfoToFirebase(db,email,hashMap)
                    }

                }
                else{

                }
            }
        }


        fun returnImageFromFirebaseStorage(storage: FirebaseStorage, email: String, isSuccessful: FirebaseInterfaces){
            val ref = storage.getReference("/images/test.test/profilePicture")
            ref.downloadUrl.addOnSuccessListener {
                isSuccessful.isSuccessful(true, it)
            }
        }

        fun saveUserToFirebase(db: FirebaseFirestore, user: User, firebaseUser: FirebaseUser?){
            user.uid = firebaseUser!!.uid
            db.collection("Users/")
                    .document(firebaseUser!!.uid).set(user)
        }

        fun addInfoToFirebase(db: FirebaseFirestore, userEmail: String, newInfo: HashMap<String,String>){
            db.collection("Users/")
                    .document(userEmail).update(newInfo as Map<String, Any>)
        }

        fun getAllUsersFromFirebase(db: FirebaseFirestore, usersListener: FirebaseUsersInterface){
            db.collection("Users").get().addOnSuccessListener {
                if(!it.isEmpty){

                    val list = ArrayList<User?>()
                    for (document: DocumentSnapshot in it){
                        list.add(document.toObject(User::class.java))
                    }

                    usersListener.onComplete(true,list)

                }
                else{

                }
            }

        }

        fun getAllUsersFromFirestore(db: FirebaseFirestore, userListener: FirebaseUsersInterface){
            db.collection("Users").addSnapshotListener(EventListener<QuerySnapshot>{
                snapshot, e->

                if (e != null) {
                    Log.w("LOG_CAT", "listen:error", e)
                    return@EventListener
                }

                var list = ArrayList<User?>()

                for (dc in snapshot!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        Log.d("LOG_CAT", "New city: " + dc.document.data)
                        val user = dc.document.toObject(User::class.java)
                        if(user.uid != FirebaseAuth.getInstance().currentUser!!.uid){
                            list.add(user)
                        }

                    }
                }

                userListener.onComplete(true,list)
            })
        }

        fun saveMessageToFirestore(db: FirebaseFirestore, user: User, message: Message, messageListener: FirestoreMessageInterface){
            val map = HashMap<String,String>()
            val random: UUID = UUID.randomUUID()

            db.collection("Messages")
                    .document(FirebaseAuth.getInstance().currentUser!!.uid)
                    .collection(user.uid!!)
                    .document()
                    .set(message)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            db.collection("Messages")
                                    .document(user.uid!!)
                                    .collection(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .document()
                                    .set(message)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful){
                                            db.collection("LatestMessage")
                                                    .document(FirebaseAuth.getInstance().currentUser!!.uid)
                                                    .collection("Message")
                                                    .document(user.uid!!)
                                                    .set(message)
                                                    .addOnCompleteListener {
                                                        if (it.isSuccessful){
                                                            db.collection("LatestMessage")
                                                                    .document(user.uid!!)
                                                                    .collection("Message")
                                                                    .document(FirebaseAuth.getInstance().currentUser!!.uid)
                                                                    .set(message)
                                                                    .addOnCompleteListener {
                                                                        if (it.isSuccessful){
                                                                            messageListener.onComplete(true)
                                                                        }
                                                                    }

                                                        }
                                                    }


                                        }
                                    }

                        }
                    }
//                    .addSnapshotListener({
//                        document,_ ->
//                        val user = document!!.toObject(User::class.java)
//                    })
        }

        fun getMessageFromFirestore(db: FirebaseFirestore, user: User, messageListener: FirestoreMessageInterface){
            val list = ArrayList<Message>()
            db.collection("Messages")
                    .document(FirebaseAuth.getInstance().currentUser!!.uid)
                    .collection(user.uid!!)
                    .orderBy("timeStamp",Query.Direction.ASCENDING)
                    .addSnapshotListener(EventListener<QuerySnapshot>{
                        snapshot, e->

                        if (snapshot == null) {
                            Log.w("LOG_CAT", "listen:error", e)
                            return@EventListener
                        }

                        for(dc: DocumentChange in snapshot.documentChanges){
                            if (dc.type == DocumentChange.Type.ADDED){
                                val message: Message = dc.document.toObject(Message::class.java)
                                list.add(message)

                            }
                        }


                        messageListener.onComplete(true,list)
                        list.clear()

//                        for(obj: QueryDocumentSnapshot in snapshot){
//                            list.add(obj.toObject(Message::class.java))
//                        }

                    })
        }

        fun getLastMessage(db: FirebaseFirestore, userInterface: FirebaseUsersInterface2){
            val list = ArrayList<Message>()
            val hashMap = HashMap<String,Message>()
            db.collection("LatestMessage")
                    .document(FirebaseAuth.getInstance().currentUser!!.uid)
                    .collection("Message")
                    .addSnapshotListener { snapshot, firebaseFirestoreException ->
                        if (snapshot == null) {
                            return@addSnapshotListener
                        }

                        for(dc: DocumentChange in snapshot.documentChanges){

                            if(dc.type == DocumentChange.Type.ADDED){
//                                list.add(dc.document.toObject(Message::class.java))
                                val obj = dc.document.toObject(Message::class.java)
                                if(obj.fromId != FirebaseAuth.getInstance().currentUser!!.uid){
                                    hashMap.put(obj.fromId.toString(),obj)
                                }
                                else if(obj.toId != FirebaseAuth.getInstance().currentUser!!.uid){
                                    hashMap.put(obj.toId.toString(),obj)
                                }

                            }

                            if(dc.type == DocumentChange.Type.MODIFIED){
//                                list.remove(dc.document.toObject(Message::class.java))
//                                list.add(dc.document.toObject(Message::class.java))
                                val obj = dc.document.toObject(Message::class.java)
                                if(obj.fromId != FirebaseAuth.getInstance().currentUser!!.uid){
                                    hashMap.put(obj.fromId.toString(),obj)
                                }
                                else if(obj.toId != FirebaseAuth.getInstance().currentUser!!.uid){
                                    hashMap.put(obj.toId.toString(),obj)
                                }

                            }
                        }


                        userInterface.onComplete2(true,hashMap)
                        }

                    }



                    }

        }

