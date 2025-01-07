package jp.ac.neec.it.k021c1460.myspiapplication

import android.util.Log
import android.widget.RadioGroup
import android.widget.TextView
import com.google.api.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import kotlin.concurrent.timer

private var timerJob: Job? = null

class Timer (private val context: ExamMainActivity){
    var totalTime = 1800
    var timeRemaining = totalTime // 秒単位

    fun secondsToMinutes(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "${minutes}分${remainingSeconds}秒"
    }

    fun startCount(timerTextView: TextView,circleProgressView: CircleProgressView){
        timeRemaining = totalTime
        timerJob = CoroutineScope(Dispatchers.Main).launch {
            // タイマーのカウントダウン処理
            while (timeRemaining > 0) {

                timerTextView.text = "残り時間: ${secondsToMinutes(timeRemaining)}"
                circleProgressView.toFloat(totalTime,timeRemaining)
                delay(1000)  // 1秒待機
                timeRemaining -= 1
            }
            //ゼロになったら
            if(timeRemaining == 0){
                timerTextView.text = "残り時間: 0 秒"
                circleProgressView.setProgress(1.0f)
                //context.moveToNext(radioGroup)
            }
            //cancelCount()
        }
    }

    fun resetTimer(){
        timeRemaining = totalTime
    }

    fun cancelCount(){
        timerJob?.cancel() // タイマーをキャンセル
        timerJob = null
    }
}