package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [examMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class examMainFragment : Fragment(R.layout.fragment_exam_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val RgOpt = view.findViewById<RadioGroup>(R.id.examRadioGroup)

        val db = Firebase.firestore

        val question = view.findViewById<TextView>(R.id.examQuestion)
        question.text = "test message"
        val examName = "模擬試験"
        val examQuesNum = 1
        val examNumber = view.findViewById<TextView>(R.id.examQuesNum)
        examNumber.text = "問題$examQuesNum"

        val examRef = db.collection("$examName").document("問題"+examQuesNum)
        examRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot != null && documentSnapshot.exists()) {
                val docData = documentSnapshot.data
                val statement = (docData?.get("問題文"))
                val question = (docData?.get("設問文"))

                val tvState = view.findViewById<TextView>(R.id.examStatement)
                val tvQuest = view.findViewById<TextView>(R.id.examQuestion)
                tvState.text = statement.toString()
                tvQuest.text = question.toString()
                Log.d(TAG, "log document Data: $docData")
            }
        }

        val examOptRef = examRef.collection("選択肢").document("選択肢")
        examOptRef.get().addOnSuccessListener { documentSnapshot ->
            //documentはマップ？
            val document = documentSnapshot.data
            Log.d(TAG,"log document option Data: $document")

            //radio button opt2 以降
            fun add_option(key:String, value: Any) {
                val RbOpt = RadioButton(this.context)
                RgOpt.addView(RbOpt)
                //文字のレイアウト
                val WC = LinearLayout.LayoutParams.WRAP_CONTENT
                val MC = LinearLayout.LayoutParams.MATCH_PARENT
                val LP = LinearLayout.LayoutParams(WC,MC)
                RbOpt.layoutParams = LP
                RbOpt.textSize = 18F
                RbOpt.setPadding(0,16,0,16)
                RbOpt.text = value.toString()
            }

            //val opt_Map = document?.filterKeys { key -> "option" in key }
            if (document != null) {
                for ((k,v) in document){
                    add_option(k,v)
                }
            }
        }
    }
}