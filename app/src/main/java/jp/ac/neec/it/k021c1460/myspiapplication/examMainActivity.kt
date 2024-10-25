package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView

class examMainActivity : AppCompatActivity() {

    private lateinit var circleProgressView: CircleProgressView
    private lateinit var timerTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_main)

        circleProgressView = findViewById(R.id.circleProgressView)
        timerTextView = findViewById(R.id.timerTextView)

        // 5秒のカウントダウンタイマーを開始
        object : CountDownTimer(60, 1) {
            override fun onTick(millisUntilFinished: Long) {
                // 残り時間を秒単位で計算
                val secondsLeft = millisUntilFinished
                timerTextView.text = "残り時間: $secondsLeft 秒"

                // 進捗を更新 (0から1の範囲)
                val progress = (5000 - millisUntilFinished) / 5000f
                circleProgressView.setProgress(progress)
            }

            override fun onFinish() {
                timerTextView.text = "残り時間: 0秒"

                // 次の画面に遷移
                val intent = Intent(this@examMainActivity, examSabActivity::class.java)
                startActivity(intent)
                finish()
            }
        }.start()

        // ホームボタンであるButtonオブジェクトを取得
        val btBack = findViewById<Button>(R.id.bt_back)
        val btNext = findViewById<Button>(R.id.bt_next)
        // リスナクラスのインスタンスを生成
        val listener = HelloListener()
        // 最初の画面に戻るボタンにリスナを設定
        btBack.setOnClickListener(listener)
        btNext.setOnClickListener(listener)

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
    //戻るボタンをタップした時の処理。
    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {
            when(view.id){
                R.id.bt_back -> {
                    finish()
                }
                R.id.bt_next -> {
                    val intent2examMainActivity= Intent(this@examMainActivity, examSabActivity::class.java)
                    startActivity(intent2examMainActivity)
                }
            }
        }
    }
}