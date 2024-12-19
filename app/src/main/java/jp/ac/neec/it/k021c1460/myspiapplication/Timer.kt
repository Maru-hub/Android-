package jp.ac.neec.it.k021c1460.myspiapplication

import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

private var timerJob: Job? = null

class Timer {
    var totalTime = 30
    var timeRemaining = totalTime // 秒単位

    fun startCount(timerTextView: TextView,circleProgressView: CircleProgressView) {
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            timeRemaining = totalTime

            // タイマーのカウントダウン処理
            while (timeRemaining > 0) {
                val secondsLeft = timeRemaining / 1000
                timerTextView.text = "残り時間: $secondsLeft 秒"
                circleProgressView.setProgress((totalTime - timeRemaining) / totalTime.toFloat())
                sleep(1000)  // 1秒待機
            timeRemaining -= 1000
            }
        }

        fun stopCount(){
            timerTextView.text = "残り時間: 0 秒"
            if (currentQuestNum <= 30) {
                Log.d("","残り時間$timeRemaining 秒、画面遷移実行")

            }
        }
    }
}