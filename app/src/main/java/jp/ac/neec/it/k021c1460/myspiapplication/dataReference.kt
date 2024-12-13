package jp.ac.neec.it.k021c1460.myspiapplication

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore

data class dataReference(val reference: DocumentReference,) {
    val db = Firebase.firestore

    //firestore参照
    val partRef = db.collection("$examName").document("問題"+currentQuestNum)
    val questRef = partRef.collection("問題").document("$questionNum")
    val userRef = db.collection("users").document("$userId")
    val userPartRef = userRef.collection("模擬試験").document("$itemName")
}