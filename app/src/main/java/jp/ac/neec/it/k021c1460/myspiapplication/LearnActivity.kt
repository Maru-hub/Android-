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
        val tvAchieveRate = findViewById<TextView>(R.id.tvAchieveRate)
        val tvCorrectAnsRate = findViewById<TextView>(R.id.tvCorrectAnsRate)
        val btBack = findViewById<Button>(R.id.bthome4)// ホームボタン
        // リスナクラスのインスタンスを生成
        val listener = HelloListener()
        // 最初の画面に戻るボタンにリスナを設定
        btBack.setOnClickListener(listener)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //言語、非言語の総問題数を数える
        fun count(toUserCount : Boolean,itemName: String){
            //変数定義
            var totalQuestNum = 0
            var userTotalQuestNum = 0
            var userCorrectNum = 0

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
                                    val docData = documentSnapshot.data
                                    Log.d("","$docData")
                                    val docDataSize = docData?.size

                                    if (docData != null) {
                                        for (questElement in docData){
                                            val isCircle = questElement.value
                                            if (isCircle == "〇"){
                                                userCorrectNum = userCorrectNum + 1
                                            }
                                        }
                                    }
                                    userTotalQuestNum = userTotalQuestNum + docDataSize!!.toInt()
                                    Log.d("", "ユーザーの$element の問題数は $docDataSize 問です。")
                                    Log.d("", "userTotalQuestNum = $userTotalQuestNum")
                                    userRef.update(itemName+"解答数",userTotalQuestNum)
                                    userRef.update(itemName+"正解数",userCorrectNum)
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
                                    Log.d("", "$element の問題数は $docData 問です。")
                                }
                            }
                        }
                    }
                }
            }
        }
        fun display (language : String){
            if (user != null){
                userRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null){
                        var userAns = documentSnapshot.get(language+"解答数")
                        var userCorrect = documentSnapshot.get(language+"正解数")
                        userAns = userAns as? Double
                        userCorrect = userCorrect as? Double

                        val totalRef = db.collection("合計問題数").document(language)
                        totalRef.get().addOnSuccessListener { documentSnapshot2 ->
                            if (documentSnapshot2 != null){
                                var totalQuest = documentSnapshot2.get("合計問題数")
                                totalQuest = totalQuest as? Double
                                if (userAns != null || userCorrect != null || totalQuest != null){
                                    tvAchieveRate.text = "${(userAns!! / totalQuest!!)*100}%"
                                    tvCorrectAnsRate.text = "${(userCorrect!! / userAns!!)*100}%"
                                }
                            }
                        }
                    }
                }
            }
        }
        //最初にユーザーデータを更新
        count(false,"言語")
        count(true,"言語")
        count(false,"非言語")
        count(true,"非言語")
        //最初は言語を表示
        display("言語")

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
                    display("言語")
                }

                R.id.learnRadioButton2 -> {
                    Log.d("radio checked", "radio checked $selectedBtText")
                    display("非言語")
                }

                R.id.learnRadioButton3 -> {
                    Log.d("radio checked", "radio checked $selectedBtText")
                    //display("模擬試験")
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