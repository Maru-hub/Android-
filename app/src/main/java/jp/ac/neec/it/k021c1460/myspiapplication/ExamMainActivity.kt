package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import kotlinx.coroutines.*

var examName = ""

class ExamMainActivity : AppCompatActivity() {

    private lateinit var circleProgressView: CircleProgressView
    private lateinit var timerTextView: TextView


    /*
    //LearnActivityへの遷移
    fun toMoveLearn(mode : String = "normal") {
        if (Timer().totalTime == 0 && mode == "kill") {
            moveLearn()
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = Dialog().show(this@ExamMainActivity)
            if (result){
                moveLearn()
            }
        }
    }

    fun moveLearn(){
        finish()
        val intent = Intent(this@ExamMainActivity, LearnActivity::class.java)
        intent.putExtra("fromExamMain", "$examName")
        startActivity(intent)
        Timer().stopCount()
    }
    */

    val timer = Timer(this@ExamMainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_main)

        circleProgressView = findViewById<CircleProgressView>(R.id.circleProgressView)
        timerTextView = findViewById(R.id.timerTextView)

        timer.startCount(timerTextView,circleProgressView)

        //examNameは前の画面のspinnerで選択させた値にする。
        examName = intent.getStringExtra("examName").toString()

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false) // 戻るボタンを非表示にする
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // 戻り値用の変数を初期値trueで用意
        var returnVal = true
        // 選択されたメニューが「戻る」の場合、アクティビティを終了。
        if (item.itemId == android.R.id.home) {
            //toMoveLearn()
        } else {
            returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
    }

    override fun onDestroy() {
        super.onDestroy()
        //タイマー終了処理
    }
}