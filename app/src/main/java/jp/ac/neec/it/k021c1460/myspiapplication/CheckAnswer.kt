package jp.ac.neec.it.k021c1460.myspiapplication

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions

class CheckAnswer {
    fun check(reference: DocumentReference, userExamDocRef: DocumentReference,
              selectedRadioButtonId: Int, selectedText: String){
        if (selectedRadioButtonId != -1) {
        Log.d("SelectedAnswer", "選択された回答: $selectedText")
        //一問目を保存する前にクリア
        reference.get().addOnSuccessListener { docmentSnapshot ->
            if (docmentSnapshot != null){
                val docData = docmentSnapshot.get("正解選択肢")
                if (selectedText == docData){
                    val data = hashMapOf("ユーザーの正誤" to "〇")
                    userExamDocRef.set(data, SetOptions.merge())
                        .addOnSuccessListener {
                            // 成功時の追加処理
                        }
                }else{
                    val data = hashMapOf("ユーザーの正誤" to "×")
                    userExamDocRef.set(data, SetOptions.merge())
                }
            }
        }
    }else{
        val data = hashMapOf("ユーザーの正誤" to "×")
        userExamDocRef.set(data, SetOptions.merge())
    }
    }
}