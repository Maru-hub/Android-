package jp.ac.neec.it.k021c1460.myspiapplication

import com.google.firebase.firestore.DocumentReference

class Reference(){
    fun correctReference(refNum : Int,examName: String): DocumentReference {
        val partRef = Firestore().get().collection("$examName").document("問題"+refNum)
        val CorrectRef = partRef.collection("選択肢").document("正解選択肢")
        return CorrectRef
    }
}