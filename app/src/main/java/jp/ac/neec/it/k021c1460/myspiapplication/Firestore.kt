package jp.ac.neec.it.k021c1460.myspiapplication

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Firestore {
    fun get(): FirebaseFirestore {
        val db = Firebase.firestore
        return db
    }
}