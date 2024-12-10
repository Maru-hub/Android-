package jp.ac.neec.it.k021c1460.myspiapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
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
        var fromExamMain = intent.getStringExtra("fromExamMain")

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
        val rtExam = findViewById<RadioButton>(R.id.learnRadioButton3)

        // リスナクラスのインスタンスを生成
        val listener = HelloListener()

        // 最初の画面に戻るボタンにリスナを設定
        btBack.setOnClickListener(listener)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //模擬試験画面からの遷移の場合選択
        if(fromExamMain != null){
            rtExam.isChecked = true
            // Spinner の参照を取得
            val spinner: Spinner = findViewById(R.id.spinner2)
            spinner.visibility = View.VISIBLE //　見えるようにする

            // データのリストを作成
            val items = listOf("模擬試験1", "模擬試験2", "模擬試験3")
            // ArrayAdapter を作成
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
            // スピナーのスタイルを設定 (ドロップダウンのスタイル)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // スピナーにアダプターをセット
            spinner.adapter = adapter

            if(fromExamMain == "模擬試験1"){spinner.setSelection(0)}
            if(fromExamMain == "模擬試験2"){spinner.setSelection(1)}
            if(fromExamMain == "模擬試験3"){spinner.setSelection(2)}
        }

        //問題解答数をカウント
        fun count(itemName : String){
            //変数定義
            var totalQuestNum = 0

            val languageRef = db.collection("$itemName")
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
                            }
                        }
                    }
                }
            }
        }

        //言語、非言語の総問題数を数える
        fun userCount(itemName: String){
            //変数定義
            var userTotalQuestNum = 0
            var userCorrectNum = 0

            val userLanguageRef = db.collection("users").document("$userId")
                .collection("$itemName")

            //get user/userId/itemName
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
                                    // 解答数の更新
                                    userRef.set(hashMapOf(itemName + "解答数" to userTotalQuestNum), SetOptions.merge())
                                    // 正解数の更新
                                    userRef.set(hashMapOf(itemName + "正解数" to userCorrectNum), SetOptions.merge())
                                }
                            }
                        }
                }
            }
        }

        fun userExamCount(examName: String) {
            // 変数定義
            var userTotalQuestNum = 0
            var userCorrectNum = 0

            val userLanguageRef = db.collection("users").document("$userId").collection("$examName")

            // 非同期タスクをリストに格納
            val tasks = mutableListOf<Task<DocumentSnapshot>>()

            userLanguageRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    userTotalQuestNum = documentSnapshot.size()

                    // 各ドキュメントを処理
                    for (document in documentSnapshot.documents) {
                        val docId = document.id

                        // 非同期タスクを追加
                        val task = userLanguageRef.document(docId).get().addOnSuccessListener { documentSnapshot2 ->
                            if (documentSnapshot2 != null) {
                                val collect = documentSnapshot2.getString("ユーザーの正誤")
                                if (collect == "〇") {
                                    userCorrectNum++
                                }
                            }
                        }
                        tasks.add(task) // タスクリストに追加
                    }

                    // すべてのタスクが完了した後に処理を続行
                    Tasks.whenAllComplete(tasks).addOnSuccessListener {
                        // 解答数の更新
                        userRef.set(hashMapOf(examName + "解答数" to userTotalQuestNum), SetOptions.merge())
                        // 正解数の更新
                        userRef.set(hashMapOf(examName + "正解数" to userCorrectNum), SetOptions.merge())
                    }
                }
            }
        }


        fun display(itemName: String) {
            if (user != null) {
                // ユーザー情報を取得
                userRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null) {
                        val userAnsOri = documentSnapshot.get(itemName + "解答数")
                        val userCorrectOri = documentSnapshot.get(itemName + "正解数")
                        val userAns = (userAnsOri as? Long)?.toDouble() ?: 0.0
                        val userCorrect = (userCorrectOri as? Long)?.toDouble() ?: 0.0

                        // 合計問題数を取得
                        val totalRef = db.collection("合計問題数").document(itemName)
                        totalRef.get().addOnSuccessListener { documentSnapshot2 ->
                            if (documentSnapshot2 != null) {
                                val totalQuestString = documentSnapshot2.get("合計問題数")
                                val totalQuest = (totalQuestString as? Long)?.toInt() ?: 0

                                // 達成率を計算してUIを更新
                                val achieveRate = if (userAns != 0.0 && totalQuest != 0) {
                                    (userAns / totalQuest) * 100
                                } else 0.0
                                tvAchieveRate.text = "%.1f".format(achieveRate) + "%"

                                // 正答率を計算してUIを更新
                                val correctAnsRate = if (userCorrect != 0.0) {
                                    if (itemName == "模擬試験1" || itemName == "模擬試験2" || itemName == "模擬試験3") {
                                        if (totalQuest != 0) (userCorrect / totalQuest) * 100 else 0.0
                                    } else {
                                        if (userAns != 0.0) (userCorrect / userAns) * 100 else 0.0
                                    }
                                } else 0.0
                                tvCorrectAnsRate.text = "%.1f".format(correctAnsRate) + "%"
                            } else {
                                // データが存在しない場合のフォールバック
                                tvAchieveRate.text = "0.0%"
                                tvCorrectAnsRate.text = "0.0%"
                            }
                        }.addOnFailureListener { e ->
                            Log.e("FirestoreError", "Error fetching total questions: ${e.message}")
                        }
                    } else {
                        Log.w("FirestoreWarning", "User data not found.")
                    }
                }.addOnFailureListener { e ->
                    Log.e("FirestoreError", "Error fetching user data: ${e.message}")
                }
            }
        }


        fun examDisplay(examName: String) {
            if (user != null) {
                // ユーザーデータの取得
                userRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null) {
                        val userAnsOri = documentSnapshot.get(examName + "解答数")
                        val userCorrectOri = documentSnapshot.get(examName + "正解数")
                        val userAns = (userAnsOri as? Long)?.toDouble() ?: 0.0
                        val userCorrect = (userCorrectOri as? Long)?.toDouble() ?: 0.0

                        // 合計問題数の取得
                        val totalRef = db.collection("合計問題数").document(examName)
                        totalRef.get().addOnSuccessListener { documentSnapshot2 ->
                            if (documentSnapshot2 != null) {
                                val totalQuestString = documentSnapshot2.get("合計問題数")
                                val totalQuest = (totalQuestString as? Long)?.toInt() ?: 30 // nullなら0を代入

                                // データ計算とUI更新
                                if (userAns != 0.0 && totalQuest != 0) {
                                    val achieveRate = (userAns / totalQuest) * 100
                                    tvAchieveRate.text = "%.1f".format(achieveRate) + "%"
                                } else {
                                    tvAchieveRate.text = "0.0%"
                                }

                                if (userCorrect != 0.0 && userAns != 0.0) {
                                    val correctAnsRate = (userCorrect / userAns) * 100
                                    tvCorrectAnsRate.text = "%.1f".format(correctAnsRate) + "%"
                                } else {
                                    tvCorrectAnsRate.text = "0.0%"
                                }
                            }
                        }.addOnFailureListener { e ->
                            Log.e("FirestoreError", "Error getting total questions: ${e.message}")
                        }
                    }
                }.addOnFailureListener { e ->
                    Log.e("FirestoreError", "Error getting user data: ${e.message}")
                }
            }
        }


        // Spinner の参照を取得
        val spinner: Spinner = findViewById(R.id.spinner2)
        // データのリストを作成
        val items = listOf("模擬試験1", "模擬試験2", "模擬試験3")
        // ArrayAdapter を作成
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        // スピナーのスタイルを設定 (ドロップダウンのスタイル)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // スピナーにアダプターをセット
        spinner.adapter = adapter
        // 選択されているアイテムを取得
        val selectedItem = spinner.selectedItem.toString() // アイテムの文字列を取得

        //最初にユーザーデータを更新
        userCount("言語")
        count("言語")
        userCount("非言語")
        count("非言語")
        userExamCount("模擬試験1")
        userExamCount("模擬試験2")
        userExamCount("模擬試験3")

        //最初は言語を表示
        //模擬試験画面からの遷移の場合模擬試験
        if(fromExamMain != null){
            if(fromExamMain == "模擬試験1"){spinner.setSelection(0)}
            if(fromExamMain == "模擬試験2"){spinner.setSelection(1)}
            if(fromExamMain == "模擬試験3"){spinner.setSelection(2)}
            examDisplay("$selectedItem")
        }else{
            display("言語")
        }

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
                    // Spinner の参照を取得
                    val spinner: Spinner = findViewById(R.id.spinner2)
                    spinner.visibility = View.INVISIBLE //　見えないようにする
                    display("言語")
                }

                R.id.learnRadioButton2 -> {
                    Log.d("radio checked", "radio checked $selectedBtText")
                    // Spinner の参照を取得
                    val spinner: Spinner = findViewById(R.id.spinner2)
                    spinner.visibility = View.INVISIBLE //　見えないようにする
                    display("非言語")
                }

                R.id.learnRadioButton3 -> {
                    Log.d("radio checked", "radio checked $selectedBtText")

                    // Spinner の参照を取得
                    val spinner: Spinner = findViewById(R.id.spinner2)
                    spinner.visibility = View.VISIBLE //　見えるようにする
                    // データのリストを作成
                    val items = listOf("模擬試験1", "模擬試験2", "模擬試験3")
                    // ArrayAdapter を作成
                    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
                    // スピナーのスタイルを設定 (ドロップダウンのスタイル)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    // スピナーにアダプターをセット
                    spinner.adapter = adapter

                    // 選択されているアイテムを取得
                    val selectedItem = spinner.selectedItem.toString() // アイテムの文字列を取得
                    userExamCount("$selectedItem")
                    examDisplay("$selectedItem")
                }
            }
        }
        // spinnerにリスナを設定
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                examDisplay(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d("Spinner", "Nothing selected")
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