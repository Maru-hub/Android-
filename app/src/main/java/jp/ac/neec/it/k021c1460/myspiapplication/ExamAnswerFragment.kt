package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

var examQuestNum = -1

class ExamAnswerFragment : Fragment(R.layout.fragment_exam_answer) {
    private var fragmentView: View? = null  // Fragment全体のViewを保持

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentView = view  // Fragment全体のViewを保持

        // RadioGroup取得
        val radioGroup = view.findViewById<RadioGroup>(R.id.examRadioGroup)
        val tvQuestion = view.findViewById<TextView>(R.id.question)
        val tvState = view.findViewById<TextView>(R.id.examStatement)
        val tvQuest = view.findViewById<TextView>(R.id.examStatement2)
        radioGroup.setOnCheckedChangeListener(RadioGroupListener())

        // Button取得
        val btNext = view.findViewById<Button>(R.id.examBtNext)
        btNext.visibility = View.INVISIBLE //次へボタンは見えなくする
        val btEnd = view.findViewById<Button>(R.id.examBtEnd)
        btNext.setOnClickListener(ButtonClickListener())
        btEnd.setOnClickListener(ButtonClickListener())
        //問題1をUIに表示
        examQuestNum = 1
        show(tvQuestion, tvState, tvQuest, radioGroup, requireContext())
    }

    fun show(tvQuestion: TextView, tvState: TextView, tvQuest: TextView, radioGroup: RadioGroup, context: Context) {
        // FragmentのUIに問題文と選択肢を表示
        Display().examQuestionDisplay(examQuestNum, tvQuestion, tvState, tvQuest, radioGroup, context)
    }

    private inner class RadioGroupListener: RadioGroup.OnCheckedChangeListener{
        override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
            if (checkedId != -1){
                val fragmentView = fragmentView ?: return
                val btNext = fragmentView.findViewById<Button>(R.id.examBtNext)
                btNext.visibility = View.VISIBLE
            }
        }
    }



    private inner class ButtonClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            when (view.id) {
                R.id.examBtNext -> {
                    // Fragment全体のViewを使ってfindViewById
                    val context = requireContext()
                    val fragmentView = fragmentView ?: return

                    val radioGroup = fragmentView.findViewById<RadioGroup>(R.id.examRadioGroup)
                    val tvQuestion = fragmentView.findViewById<TextView>(R.id.question)
                    val tvState = fragmentView.findViewById<TextView>(R.id.examStatement)
                    val tvQuest = fragmentView.findViewById<TextView>(R.id.examStatement2)

                    //問題の正誤を保存する
                    val userExamRef = User().userExamRef()
                    val selectedId = radioGroup.checkedRadioButtonId
                    val selectedButton = fragmentView.findViewById<RadioButton>(selectedId)
                    val selectedText = selectedButton.text.toString()
                    CheckAnswer().check(Reference().correctReference(examQuestNum,examName),userExamRef,
                        selectedId,selectedText)

                    //問題を変える前に選択肢を消去
                    radioGroup.removeAllViews()
                    //問題番号をインクリメントして新しい問題を表示
                    examQuestNum += 1
                    show(tvQuestion, tvState, tvQuest, radioGroup, context)
                    //次へボタンを見えなくする
                    val btNext = fragmentView.findViewById<Button>(R.id.examBtNext)
                    btNext.visibility = View.INVISIBLE
                }
                R.id.examBtEnd -> {
                    // ボタンの終了処理
                    val context = requireContext()
                    CoroutineScope(Dispatchers.Main).launch {
                        val result = Dialog().show(context)
                        println(result)
                        if(result) {
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }
}
