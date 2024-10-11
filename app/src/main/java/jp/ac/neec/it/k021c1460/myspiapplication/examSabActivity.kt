package jp.ac.neec.it.k021c1460.myspiapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.CountDownTimer
import android.widget.TextView

class examSabActivity : AppCompatActivity() {

    private lateinit var circleProgressView: CircleProgressView
    private lateinit var timerTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_sab)


        circleProgressView = findViewById(R.id.circleProgressView)
        timerTextView = findViewById(R.id.timerTextView)

        // 5秒のカウントダウンタイマーを開始
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // 残り時間を秒単位で計算
                val secondsLeft = millisUntilFinished / 1000
                timerTextView.text = "残り時間: $secondsLeft 秒"

                // 進捗を更新 (0から1の範囲)
                val progress = (5000 - millisUntilFinished) / 5000f
                circleProgressView.setProgress(progress)
            }

            override fun onFinish() {
                timerTextView.text = "残り時間: 0秒"

                // 次の画面に遷移
                val intent = Intent(this@examSabActivity, NextQuestionActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()
    }
}