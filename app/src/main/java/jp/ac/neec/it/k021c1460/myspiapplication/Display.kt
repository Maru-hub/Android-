package jp.ac.neec.it.k021c1460.myspiapplication

import android.text.Html
import android.util.Log
import android.widget.RadioGroup
import android.widget.TextView
import com.google.api.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Display {
    fun examQuestionDisplay(examQuestNum: Int,TvNumber: TextView,TvState: TextView,TvQuest: TextView,radioGroup: RadioGroup,context: android.content.Context){
        var exam = Exam(examName,examQuestNum)

        //問題番号を表示
        TvNumber.text = "問題"+examQuestNum
        //問題文と設問文を取得
        CoroutineScope(Dispatchers.Main).launch {
            //問題設問文取得
            val statement = exam.getStatement()
            //選択肢を取得
            val selection = exam.getSelection()

            Log.d("", "$statement $selection")

            //TextViewに表示
            TvState.text = Html.fromHtml(statement.first, Html.FROM_HTML_MODE_COMPACT)
            TvQuest.text = Html.fromHtml(statement.second, Html.FROM_HTML_MODE_COMPACT)

            //選択肢表示radio button追加
            if (selection != null) {
                for ((k, v) in selection) {
                    RadioButton().add(k, v,radioGroup,context)
                }
            }
        }
    }
}