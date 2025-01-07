package jp.ac.neec.it.k021c1460.myspiapplication

import android.os.Bundle
import android.util.Log
import android.content.ContentValues.TAG
import android.content.Intent
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AnswerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_answer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val itemName = intent.getStringExtra("itemName")
        val itemWhich = intent.getStringExtra("itemWhich")
        val buttonText = intent.getStringExtra("questionNum")
        // ホームボタンであるButtonオブジェクトを取得
        val btNext = findViewById<Button>(R.id.bt_nextbottn)
        // リスナクラスのインスタンスを生成
        val listener = HelloListener()
        // 最初の画面に戻るボタンにリスナを設定
        btNext.setOnClickListener(listener)

        val db = Firebase.firestore

        val RgOpt = findViewById<RadioGroup>(R.id.radioGroup)

        val partRef = db.collection("$itemWhich").document("$itemName")
        val questRef = partRef.collection("問題").document("$buttonText")

        questRef.get().addOnSuccessListener { documentSnapshot ->
            val docData = documentSnapshot.data
            val statement = (docData?.get("問題文"))
            val question = (docData?.get("設問文"))

            val tvState = findViewById<TextView>(R.id.statement)
            val tvQuest = findViewById<TextView>(R.id.question)
            tvState.text = Html.fromHtml(statement.toString(), Html.FROM_HTML_MODE_COMPACT)
            tvQuest.text = Html.fromHtml(question.toString(), Html.FROM_HTML_MODE_COMPACT)
            //val keys = document?.keys
            Log.d(TAG,"log document Data: $docData")
        }

        val optRef = questRef.collection("選択肢").document("選択肢")
        optRef.get().addOnSuccessListener { documentSnapshot ->
            //documentはマップ？
            val document = documentSnapshot.data
            Log.d(TAG,"log document option Data: $document")

            //radio button opt2 以降
            fun add_option(key:String, value: Any) {
                val RbOpt = RadioButton(this)
                RgOpt.addView(RbOpt)
                //文字のレイアウト
                val WC = LinearLayout.LayoutParams.WRAP_CONTENT
                val MC = LinearLayout.LayoutParams.MATCH_PARENT
                val LP = LinearLayout.LayoutParams(WC,MC)
                RbOpt.layoutParams = LP
                RbOpt.textSize = 18F
                RbOpt.setPadding(0,16,0,16)
                RbOpt.text = value.toString()
            }

            //val opt_Map = document?.filterKeys { key -> "option" in key }
            if (document != null) {
                for ((k,v) in document){
                    add_option(k,v)
                }
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    //解答を表示ボタンをタップした時の処理。
    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {
            val RgOpt = findViewById<RadioGroup>(R.id.radioGroup)
            when (view.id) {
                R.id.bt_nextbottn -> {

                    // ラジオグループから選択されたボタンのIDを取得
                    val selectedRadioButtonId = RgOpt.checkedRadioButtonId
                    if (selectedRadioButtonId != -1) {

                        // 選択されたラジオボタンを取得
                        val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                        val selectedAnswer = selectedRadioButton.text.toString()

                        // Intentに選択された解答を渡す
                        val intentAnswerActivity =
                            Intent(this@AnswerActivity, ExplanationActivity::class.java)
                        // intentで itemName, itemWhich, questionNum (ボタンのテキスト) を渡す
                        intentAnswerActivity.putExtra("itemName", intent.getStringExtra("itemName"))
                        intentAnswerActivity.putExtra("itemWhich", intent.getStringExtra("itemWhich"))
                        intentAnswerActivity.putExtra("questionNum", intent.getStringExtra("questionNum"))
                        intentAnswerActivity.putExtra("selectedAnswer", selectedAnswer)
                        startActivity(intentAnswerActivity)
                        finish()
                    }
                }
            }
        }
    }
    }
