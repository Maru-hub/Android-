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
    var totalTime = 10
    var timeRemaining = totalTime // 秒単位

    fun startCount(timerTextView: TextView,circleProgressView: CircleProgressView) {
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            timeRemaining = totalTime
            // タイマーのカウントダウン処理
            while (timeRemaining > 0) {
                val secondsLeft = timeRemaining / 1000
                timerTextView.text = "残り時間: $secondsLeft 秒"
                circleProgressView.setProgress((totalTime - timeRemaining) / totalTime.toFloat())
                Log.d("","float ${(totalTime - timeRemaining) / totalTime.toFloat()}")
                sleep(1000)  // 1秒待機
            timeRemaining -= 1
            }
            stopCount(timerTextView)
        }
    }
    fun stopCount(timerTextView: TextView){
        timerTextView.text = "残り時間: 0 秒"
        timerJob?.cancel() // タイマーをキャンセル
        timerJob = null
        timerTextView.text = "タイマーストップ"
    }
}