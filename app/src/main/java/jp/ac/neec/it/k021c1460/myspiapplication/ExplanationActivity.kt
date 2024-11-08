package jp.ac.neec.it.k021c1460.myspiapplication

import android.os.Bundle
import android.util.Log
import android.content.ContentValues.TAG
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import android.text.Html
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore

class ExplanationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_explanation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ホームボタンであるButtonオブジェクトを取得
        val btHomeBack = findViewById<Button>(R.id.bt_homeback)
        // リスナクラスのインスタンスを生成
        val listener = HelloListener()
        // 最初の画面に戻るボタンにリスナを設定
        btHomeBack.setOnClickListener(listener)

        val db = Firebase.firestore

        val itemName = intent.getStringExtra("itemName")
        val itemWhich = intent.getStringExtra("itemWhich")
        val questionNum = intent.getStringExtra("questionNum")
        val selectedAnswer = intent.getStringExtra("selectedAnswer")
        val tvAnswer = findViewById<TextView>(R.id.youanswer)
        tvAnswer.text = selectedAnswer.toString()

        val partRef = db.collection("$itemWhich").document("$itemName")
        val questRef = partRef.collection("問題").document("$questionNum")
        questRef.get().addOnSuccessListener { documentSnapshot ->
            val docData = documentSnapshot.data
            val AnswerStr = docData?.get("解説文") as String
            Log.d(TAG, "DocumentSnapshot data: $AnswerStr")
            val tvAnswer = findViewById<TextView>(R.id.textView30)
            tvAnswer.text = Html.fromHtml(AnswerStr, Html.FROM_HTML_MODE_COMPACT)
        }
        val choiRef = questRef.collection("選択肢").document("正解選択肢")
        choiRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                val docData = documentSnapshot.data
                val correctAns = docData?.get("正解選択肢")
                Log.d(TAG, "DocumentSnapshot data: $correctAns")
                val tvCorrect = findViewById<TextView>(R.id.correctanswertext)
                tvCorrect.text = correctAns.toString()

                val auth = com.google.firebase.ktx.Firebase.auth
                val user = auth.currentUser
                val userId = user?.uid

                val db = com.google.firebase.ktx.Firebase.firestore
                val userRef = db.collection("users").document("$userId")
                val partRef = userRef.collection("$itemWhich").document("$itemName")
                val questRef = partRef.collection("問題").document("$questionNum")

                val TvSeigo = findViewById<TextView>(R.id.TvMaruBatu)

                if (selectedAnswer == correctAns){
                    TvSeigo.text = "〇"
                    val data = hashMapOf("ユーザーの正誤" to "〇")
                    questRef.set(data)
                }
                else{
                    TvSeigo.text = "×"
                    val data = hashMapOf("ユーザーの正誤" to "×")
                    questRef.set(data)
                }

            } else {
                Log.d(TAG, "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 戻り値用の変数を初期値trueで用意
        var returnVal = true
        // 選択されたメニューが「戻る」の場合、アクティビティを終了。
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
    }
    //戻るボタンをタップした時の処理。
    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {
            when(view.id){
                R.id.bt_homeback -> {
                    finish()
                }
            }
        }
    }
}