package jp.ac.neec.it.k021c1460.myspiapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


class LearnActivity : AppCompatActivity() {
    var partCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        // ホームボタンであるButtonオブジェクトを取得
        val btBack = findViewById<Button>(R.id.bthome4)
        // リスナクラスのインスタンスを生成
        val listener = HelloListener()
        // 最初の画面に戻るボタンにリスナを設定
        btBack.setOnClickListener(listener)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val radioGroup = findViewById<RadioGroup>(R.id.learnRadioGroup)
        val checkedBt = radioGroup.checkedRadioButtonId
        val selectedRadioBt = findViewById<RadioButton>(checkedBt)
        val selectedBtText = selectedRadioBt.text.toString()

        if (checkedBt != -1){
            when(selectedBtText){
                "言語" ->{
                    Log.d("radio checked","radio checked $selectedBtText")
                }
                "非言語" ->{
                    Log.d("radio checked","radio checked $selectedBtText")
                }
                "模擬試験" ->{
                    Log.d("radio checked","radio checked $selectedBtText")
                }
            }
        }

        radioGroup.setOnCheckedChangeListener { group, selected ->
            val radioGroup = findViewById<RadioGroup>(R.id.learnRadioGroup)
            val checkedBt = radioGroup.checkedRadioButtonId
            val selectedRadioBt = findViewById<RadioButton>(checkedBt)
            val selectedBtText = selectedRadioBt.text.toString()
            Log.d("radio checked", "radio checked $selectedBtText")

            //合計問題数とカレントユーザーの解答数を取得
            val db = Firebase.firestore

            val partRef = db.collection("合計問題数").document("$selectedBtText")
            partRef.get().addOnSuccessListener { documentSnapshot ->
             if (documentSnapshot != null){
                 val docData = documentSnapshot.getString("合計問題数")
             }
            }

            val user = Firebase.auth
            val currentUser = user.currentUser
            val userId = currentUser?.uid
            val userAnsRef = db.collection("users").document("$userId")
            //val secondRef = userAnsRef.
        }
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
                R.id.bthome4 -> {
                    finish()
                }
            }
        }
    }
}