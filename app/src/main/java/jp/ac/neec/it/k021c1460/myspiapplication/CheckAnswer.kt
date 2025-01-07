package jp.ac.neec.it.k021c1460.myspiapplication

import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions

class CheckAnswer {
    fun check(answerRef: DocumentReference, userAnswerRef: DocumentReference,
              selectedRadioButtonId: Int = -1, selectedText: String = "no select"){
        if (selectedRadioButtonId != -1) {
            Log.d("SelectedAnswer", "選択された回答: $selectedText")
            //一問目を保存する前にクリア
            answerRef.get().addOnSuccessListener { docmentSnapshot ->
                if (docmentSnapshot != null){
                    val docData = docmentSnapshot.get("正解選択肢")
                    if (selectedText == docData){
                        val data = hashMapOf("ユーザーの正誤" to "〇")
                        set(userAnswerRef,data)

                    }else{
                        //何も選択されていない場合は×
                        val data = hashMapOf("ユーザーの正誤" to "×")
                        set(userAnswerRef,data)
                    }
                }
            }
        }else{
            val data = hashMapOf("ユーザーの正誤" to "×")
            set(userAnswerRef,data)
        }
    }

    fun set(ref: DocumentReference, data: HashMap<String, String>) {
        ref.set(data, SetOptions.merge())
            .addOnSuccessListener {
                // 成功した場合の処理
                println("Document successfully written! by CheckAnswer Class")
            }
            .addOnFailureListener { e ->
                // 失敗した場合の処理
                println("Error writing document: ${e.message} by CheckAnswer Class")
            }
    }

}