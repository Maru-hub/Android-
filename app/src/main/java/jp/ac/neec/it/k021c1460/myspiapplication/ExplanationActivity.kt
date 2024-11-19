package jp.ac.neec.it.k021c1460.myspiapplication

import android.os.Bundle
import android.util.Log
import android.content.ContentValues.TAG
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
import com.google.firebase.firestore.SetOptions

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

        //　オブジェクトを取得
        val btHomeBack = findViewById<Button>(R.id.bt_homeback)
        val TvSeigo = findViewById<TextView>(R.id.TvMaruBatu)
        val tvCorrect = findViewById<TextView>(R.id.correctanswertext)
        // リスナクラスのインスタンスを生成
        val listener = HelloListener()
        // 最初の画面に戻るボタンにリスナを設定
        btHomeBack.setOnClickListener(listener)
        //bundle 取得
        val itemName = intent.getStringExtra("itemName")
        val itemWhich = intent.getStringExtra("itemWhich")
        val questionNum = intent.getStringExtra("questionNum")
        val selectedAnswer = intent.getStringExtra("selectedAnswer")

        val tvAnswer = findViewById<TextView>(R.id.youanswer)
        tvAnswer.text = selectedAnswer.toString()
        //firebaseユーザー
        val auth = com.google.firebase.ktx.Firebase.auth
        val user = auth.currentUser
        val userId = user?.uid
        //firestoreの参照
        val db = Firebase.firestore
        val partRef = db.collection("$itemWhich").document("$itemName")
        val questRef = partRef.collection("問題").document("$questionNum")
        val userRef = db.collection("users").document("$userId")
        val userPartRef = userRef.collection("$itemWhich").document("$itemName")
        val choiRef = questRef.collection("選択肢").document("正解選択肢")

        questRef.get().addOnSuccessListener { documentSnapshot ->
            val docData = documentSnapshot.data
            val AnswerStr = docData?.get("解説文") as String
            Log.d(TAG, "DocumentSnapshot data: $AnswerStr")
            val tvAnswer = findViewById<TextView>(R.id.textView30)
            tvAnswer.text = Html.fromHtml(AnswerStr, Html.FROM_HTML_MODE_COMPACT)
        }

        //問題数を取得
        fun achRate(answered: Int, total: Int): String {
            val num = (answered.toDouble() / total.toDouble() * 100)
            Log.d("","achievement :$answered : $total : $num")
            val returnVar = "${answered-1} / $total"
            return returnVar
        }

        partRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null){
                val docData = documentSnapshot.data
                val listCount = docData?.count()
                Log.d("answered","answered is $listCount")
            }
        }

        //答え合わせ
        choiRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null) {
                val docData = documentSnapshot.data
                val correctAns = docData?.get("正解選択肢")
                Log.d(TAG, "正解: $correctAns")
                tvCorrect.text = correctAns.toString()

                if (selectedAnswer == correctAns){
                    TvSeigo.text = "〇"
                    val data = hashMapOf("$questionNum" to "〇")
                    userPartRef.set(data, SetOptions.merge())
                }
                else{
                    TvSeigo.text = "×"
                    val data = hashMapOf("$questionNum" to "×")
                    userPartRef.set(data, SetOptions.merge())
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