package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Context
import android.util.Log
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class Exam(examName: String, number: Int) {
    val db = Firestore().db
    val examRef = db.collection("$examName").document("問題"+number)
    val examOptRef = examRef.collection("選択肢").document("選択肢")

    var statement: Any? = ""
    var question: Any? = ""
    var document: Map<String, Any>? = null

    suspend fun getStatement(): Pair<String,String> {
        return try {
            val examText = examRef.get().await()
            if(examText.exists()){
                statement = (examText.data?.get("問題文"))
                question = (examText.data?.get("設問文"))
                //正常
                Pair(statement.toString(),question.toString())
            }else{
                //getができなかった場合
                Pair("エラー", "ドキュメントが存在しません")
            }
        } catch (e: Exception){
            //エラーが起きた場合
            Pair("エラー問題が取得できませんでした","$e")
        }
    }

    suspend fun getSelection(): Map<String, Any>? {
        val examSelection = examOptRef.get().await()
        return examSelection.data
    }
}