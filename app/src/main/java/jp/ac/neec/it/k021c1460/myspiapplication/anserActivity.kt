package jp.ac.neec.it.k021c1460.myspiapplication
//package jp.ac.neec.it.k021c1348.a20240716project1

import android.os.Bundle
import android.util.Log
import android.content.ContentValues.TAG
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_anser)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = Firebase.firestore

        val RgOpt = findViewById<RadioGroup>(R.id.radioGroup)

        val partRef = db.collection("非言語").document("組み合わせ")
        val questRef = partRef.collection("問題1").document("文章")

        questRef.get().addOnSuccessListener { documentSnapshot ->
            val docData = documentSnapshot.data
            val statement = (docData?.get("設問文"))
            val question = (docData?.get("問題文"))

            val tvState = findViewById<TextView>(R.id.statement)
            val tvQuest = findViewById<TextView>(R.id.question)
            tvState.text = statement.toString()
            tvQuest.text = question.toString()
            //val keys = document?.keys
            Log.d(TAG,"log document Data: $docData")
        }


        val optRef = partRef.collection("問題1").document("選択肢")
        optRef.get().addOnSuccessListener { documentSnapshot ->
            //documentはマップ？
            val document = documentSnapshot.data
            Log.d(TAG,"log document option Data: $document")

            //radio button opt2 以降
            fun add_option(key:String, value: Any) {
                val RbOpt = RadioButton(this)
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

            //button listener
            class HelloListener : View.OnClickListener {
                override fun onClick(view: View){

                    val intentActivity2 = Intent(this@MainActivity2,MainActivity2::class.java)
                    startActivity(intentActivity2)
                }
            }
            val btClick = findViewById<Button>(R.id.button2)
            val listener = HelloListener()
            btClick.setOnClickListener(listener)
        }
    }
}
class anserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_anser)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}