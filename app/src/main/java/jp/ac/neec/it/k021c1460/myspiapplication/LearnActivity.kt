package jp.ac.neec.it.k021c1460.myspiapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore


class LearnActivity : AppCompatActivity() {
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

        val db = Firebase.firestore

        val radioGroup = findViewById<RadioGroup>(R.id.learnRadioGroup)
        val checkedBt = radioGroup.checkedRadioButtonId
        val selectedRadioBt = findViewById<RadioButton>(checkedBt)
        val selectedBtText = selectedRadioBt.text.toString()

        //firebaseユーザー
        val auth = com.google.firebase.ktx.Firebase.auth
        val user = auth.currentUser
        val userId = user?.uid
        //bundle 取得
        val itemName = intent.getStringExtra("itemName")
        val itemWhich = intent.getStringExtra("itemWhich")
        val questionNum = intent.getStringExtra("questionNum")
        val selectedAnswer = intent.getStringExtra("selectedAnswer")
        //firestore参照
        val partRef = db.collection("$itemWhich").document("$itemName")
        val questRef = partRef.collection("問題").document("$questionNum")
        val userRef = db.collection("users").document("$userId")
        val userPartRef = userRef.collection("$itemWhich").document("$itemName")
        val choiRef = questRef.collection("選択肢").document("正解選択肢")
        //画面部品
        val TvAchievement = findViewById<TextView>(R.id.tvrate)

        //言語、非言語の総問題数を数える
        fun count(toUserCount : Boolean,itemName: String){
            var totalQuestNum = 0
            var userTotalQuestNum = 0
            val languageRef = db.collection("$itemName")
            val userLanguageRef = db.collection("users").document("$userId")
                .collection("$itemName")
            //get
            if (toUserCount){
                userLanguageRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null) {
                        val list: MutableList<String> = mutableListOf()
                        for (document in documentSnapshot) {
                            list.add(document.id)
                        }
                        for (element in list) {
                            val quesNumRef = userLanguageRef.document("$element")
                            //get
                            quesNumRef.get().addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot != null) {
                                    val docDataSize = documentSnapshot.data?.size
                                    userTotalQuestNum = userTotalQuestNum + docDataSize!!.toInt()
                                    Log.d("", "ユーザーの$element の問題数は $docDataSize 問です。")
                                    Log.d("", "userTotalQuestNum = $userTotalQuestNum")
                                    userRef.update(itemName+"解答数",userTotalQuestNum)
                                }
                            }

                        }

                    }
                }
            }else{
                languageRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null) {
                        val list: MutableList<String> = mutableListOf()
                        for (document in documentSnapshot) {
                            list.add(document.id)
                        }
                        for (element in list) {
                            val quesNumRef = languageRef.document("$element")
                            //get
                            quesNumRef.get().addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot != null) {
                                    val docData = documentSnapshot.getString("問題数")
                                    totalQuestNum = totalQuestNum + docData!!.toInt()
                                    Log.d("", "$element の問題数は $docData 問です。"
                                    )
                                }
                            }
                        }
                    }
                }
            }
            //val displayRate = userTotalQuestNum.toDouble() / totalQuestNum * 100
            TvAchievement.text = "$userTotalQuestNum / $totalQuestNum"
        }

        //最初は言語の学習状況を表示(初期設定で言語が最初に選択されている)
        count(false,"言語")
        count(true,"言語")

        radioGroup.setOnCheckedChangeListener { group, selected ->
            val radioGroup = findViewById<RadioGroup>(R.id.learnRadioGroup)
            val checkedBt = radioGroup.checkedRadioButtonId
            val selectedRadioBt = findViewById<RadioButton>(checkedBt)
            val selectedBtText = selectedRadioBt.text.toString()
            Log.d("radio checked", "radio checked $selectedBtText")
            //リスナが反応したら押下ボタン判定
            when (selected) {
                R.id.learnRadioButton -> {
                    Log.d("radio checked", "radio checked $selectedBtText")
                    count(false,"言語")
                    count(true,"言語")

                }

                R.id.learnRadioButton2 -> {
                    Log.d("radio checked", "radio checked $selectedBtText")
                    count(false,"非言語")
                    count(true,"非言語")
                }

                R.id.learnRadioButton3 -> {
                    Log.d("radio checked", "radio checked $selectedBtText")
                }
            }
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