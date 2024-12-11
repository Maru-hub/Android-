package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions

var currentQuestNum = 0
class ExamMainActivity : AppCompatActivity() {

    private lateinit var circleProgressView: CircleProgressView
    private lateinit var timerTextView: TextView
    private var countDownTimer: CountDownTimer? = null  // タイマーを保持する変数

    //ユーザー認証
    val auth = Firebase.auth
    val user = auth.currentUser
    val userId = user?.uid

    //firebase
    val db = Firebase.firestore

    //firestore参照
    //val partRef = db.collection("模擬試験").document("問題"+currentQuestNum)
    //val questRef = partRef.collection("問題").document("$questionNum")
    val userRef = db.collection("users").document("$userId")
    //val userPartRef = userRef.collection("模擬試験").document("$itemName")

    //変数
    var selectedExam = ""

    //LearnActivityへの遷移
    fun toMoveLearn(){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("確認")
            .setMessage("模擬試験を終了しますよろしいですか？")
            .setPositiveButton("はい") { _, _ ->
                // 試験開始画面に遷移
                finish()
                val intent = Intent(this@ExamMainActivity, LearnActivity::class.java)
                intent.putExtra("fromExamMain","$selectedExam")
                startActivity(intent)
            }
                .setNegativeButton("いいえ", null) //何もしない
                .show()
    }

    fun correctReference(refNum : Int): DocumentReference {
        val partRef = db.collection("$examName").document("問題"+refNum)
        val CorrectRef = partRef.collection("選択肢").document("正解選択肢")
        return CorrectRef
    }


    var examName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_main)

        currentQuestNum += 1

        val RgOpt = findViewById<RadioGroup>(R.id.examRadioGroup)

        val examQuestNum = currentQuestNum
        Log.d(TAG,"examQuestNum = $examQuestNum")

        val question = findViewById<TextView>(R.id.examQuestion)
        question.text = "test message"
        //examNameは前の画面のspinnerで選択させた値にする。
        selectedExam = intent.getStringExtra("examNum").toString()

        if (selectedExam != null) {
            examName = selectedExam
        }

        val examNumber = findViewById<TextView>(R.id.examQuesNum)
        examNumber.text = "問題$examQuestNum"

        val examRef = db.collection("$examName").document("問題"+examQuestNum)
        examRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val docData = documentSnapshot.data
                val statement = (docData?.get("問題文"))
                val question = (docData?.get("設問文"))

                val tvState = findViewById<TextView>(R.id.examStatement)
                val tvQuest = findViewById<TextView>(R.id.examQuestion)

                tvState.text = Html.fromHtml(statement.toString(), Html.FROM_HTML_MODE_COMPACT)
                tvQuest.text = Html.fromHtml(question.toString(), Html.FROM_HTML_MODE_COMPACT)
                Log.d(TAG, "log document Data: $docData")
            }
            else{
                //問題がデータベースにない(全問出題し終わった)時の処理をここに書く
                toMoveLearn()
            }
        }


        val examOptRef = examRef.collection("選択肢").document("選択肢")
        examOptRef.get().addOnSuccessListener { documentSnapshot ->
            //documentはマップ？
            val document = documentSnapshot.data
            Log.d(TAG, "log document option Data: $document")

            //radio button opt2 以降
            fun add_option(key: String, value: Any) {
                val RbOpt = RadioButton(this)
                RgOpt.addView(RbOpt)
                //文字のレイアウト
                val WC = LinearLayout.LayoutParams.WRAP_CONTENT
                val MC = LinearLayout.LayoutParams.MATCH_PARENT
                val LP = LinearLayout.LayoutParams(WC, MC)
                RbOpt.layoutParams = LP
                RbOpt.textSize = 18F
                RbOpt.setPadding(0, 16, 0, 16)
                RbOpt.text = value.toString()
            }

            //val opt_Map = document?.filterKeys { key -> "option" in key }
            if (document != null) {
                for ((k, v) in document) {
                    add_option(k, v)
                }
            }

            circleProgressView = findViewById(R.id.circleProgressView)
            timerTextView = findViewById(R.id.timerTextView)

            startCountDownTimer()

            // ホームボタンであるButtonオブジェクトを取得
            val btBack = findViewById<Button>(R.id.bt_back)
            val btNext = findViewById<Button>(R.id.bt_next)
            // リスナクラスのインスタンスを生成
            val listener = HelloListener()
            btBack.setOnClickListener(listener)
            btNext.setOnClickListener(listener)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    // カウントダウンタイマーの開始メソッド
    private fun startCountDownTimer() {
        // 5秒のカウントダウンタイマーを開始
        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                timerTextView.text = "残り時間: $secondsLeft 秒"
                val progress = (5000 - millisUntilFinished) / 5000f
                circleProgressView.setProgress(progress)
            }

            override fun onFinish() {
                timerTextView.text = "残り時間: 0秒"
                toMoveLearn()
            }
        }.start()
    }

    // 次の画面に遷移するメソッド
    private fun moveToNextScreen() {

        val userExamDocRef = db.collection("users").document("$userId").
        collection("$examName").document("問題$currentQuestNum")

        // RadioGroupから選択されたラジオボタンのテキストを取得
        val radioGroup = findViewById<RadioGroup>(R.id.examRadioGroup)  // RadioGroupのIDを指定
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId

        fun delete(){
            val userExamRef = db.collection("users").document("$userId").
            collection("$examName")
            userExamRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null){
                    for (document in documentSnapshot) {
                        document.reference.delete()
                    }
                }
            }
        }
        // 選択されたRadioButtonが存在する場合
        if (selectedRadioButtonId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
            val selectedText = selectedRadioButton.text.toString()  // 選択されたラジオボタンのテキストを取得
            Log.d("SelectedAnswer", "選択された回答: $selectedText")
            //一問目を保存する前にクリア
            if (currentQuestNum == 1){ delete() }
            correctReference(currentQuestNum).get().addOnSuccessListener { docmentSnapshot ->
                if (docmentSnapshot != null){
                    val docData = docmentSnapshot.get("正解選択肢")
                    if (selectedText == docData){
                        val data = hashMapOf("ユーザーの正誤" to "〇")
                        userExamDocRef.set(data, SetOptions.merge())
                            .addOnSuccessListener {
                                Log.d("Firestore", "mosi de-ta hozonn kannryou")
                                // 成功時の追加処理
                            }
                    }else{
                        val data = hashMapOf("ユーザーの正誤" to "×")
                        userExamDocRef.set(data, SetOptions.merge())
                    }
                }
            }
        }
        //今の画面を終了した後新たな画面を作成
        finish()
        val intent = Intent(this@ExamMainActivity, ExamMainActivity::class.java)
        intent.putExtra("examNum",examName)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 戻り値用の変数を初期値trueで用意
        var returnVal = true
        // 選択されたメニューが「戻る」の場合、アクティビティを終了。
        if (item.itemId == android.R.id.home) {
            countDownTimer?.cancel()  // 戻るボタン押下時にタイマーをキャンセル
            finish()
        } else {
            returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
    }

    //ボタンをタップした時の処理。
    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {
            when(view.id){
                R.id.bt_back -> {
                    countDownTimer?.cancel()  // バックボタン押下時にタイマーをキャンセル
                    toMoveLearn()
                }
                R.id.bt_next -> {
                    countDownTimer?.cancel()  // 次へボタン押下時にタイマーをキャンセル
                    val radioGroup = findViewById<RadioGroup>(R.id.examRadioGroup)
                    val selectedId = radioGroup.checkedRadioButtonId
                    //選択されていない場合は遷移しない
                    if (selectedId != -1) {
                        moveToNextScreen()  // 同じ画面に戻る
                    }

                }
            }
        }
    }

    // アクティビティが破棄される際に、タイマーがまだ動作している場合に備えてキャンセル
    override fun onDestroy() {
        countDownTimer?.cancel()  // アクティビティ終了時にタイマーをキャンセル
        super.onDestroy()
    }
}