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
    private var countDownTimer: CountDownTimer? = null  // タイマーを保持する変数
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_main)

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
                moveToNextScreen()
            }
        }.start()
    }

    // 次の画面に遷移するメソッド
    private fun moveToNextScreen() {
        // タイマーをキャンセルして次の画面に遷移
        countDownTimer?.cancel()
        val intent = Intent(this@examMainActivity, examMainActivity::class.java)
        startActivity(intent)
        finish()
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

    //戻るボタンをタップした時の処理。
    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {
            when(view.id){
                R.id.bt_back -> {
                    countDownTimer?.cancel()  // バックボタン押下時にタイマーをキャンセル
                    finish()
                }
                R.id.bt_next -> {
                    countDownTimer?.cancel()  // 次へボタン押下時にタイマーをキャンセル
                    moveToNextScreen()  // 同じ画面に戻る
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