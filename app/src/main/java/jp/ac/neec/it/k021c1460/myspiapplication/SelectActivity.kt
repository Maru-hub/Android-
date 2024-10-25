package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class SelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_select)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = Firebase.firestore
        val itemName = intent.getStringExtra("itemName")
        val itemWhich = intent.getStringExtra("itemWhich")
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        val tv8 = findViewById<TextView>(R.id.textView8)
        tv8.text = itemName



        fun addButton(buttonsNum: Int) {
            val totalButtonNum = if (buttonsNum % 3 == 0) {
                (buttonsNum / 3) * 3
            } else {
                (buttonsNum / 3 + 1) * 3
            }
            val buttonsPerRow = 3

            for (i in 0 until totalButtonNum step buttonsPerRow) {
                val tableRow = TableRow(this)

                for (j in 0 until buttonsPerRow) {
                    val button = Button(this)
                    val buttonIndex = i + j + 1

                    if (buttonIndex <= buttonsNum) {
                        button.text = "問題"+buttonIndex  // ボタンに番号を付ける
                        button.setBackgroundColor(Color.parseColor("#1972a4"))
                        // リスナクラスのインスタンスを生成
                        val listener = HelloListener()
                        // 最初の画面に戻るボタンにリスナを設定
                        button.setOnClickListener(listener)
                    } else {
                        // 余分なボタンを非表示にする
                        button.alpha = 0f
                        button.isClickable = false
                    }

                    val layoutParams = TableRow.LayoutParams(
                        0, 96, 0.3f
                    ).apply { setMargins(8, 8, 8, 8) } // 左右に8dpのマージンを設定
                    // ボタンにレイアウトパラメータをセット
                    button.layoutParams = layoutParams

                    tableRow.addView(button)
                }
                // TableRowをTableLayoutに追加
                tableLayout.addView(tableRow)
            }
        }


        val quesRef = db.collection("$itemWhich").document("$itemName")
        quesRef.get().addOnSuccessListener { documentSnapshot ->
                val docData = documentSnapshot.data
                val questNum = docData?.get("問題数") as String
                Log.d(TAG, "questNum :$questNum")
                if (questNum != null) {
                    addButton(questNum.toInt())
                }
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
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
    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {

            // 押されたボタンを Button 型にキャスト
            val button = view as Button
            // ボタンのテキストを取得
            val buttonText = button.text.toString()

            // ボタンのテキストを次のアクティビティに渡す
            val intentanser = Intent(this@SelectActivity, AnswerActivity::class.java)
            // intentで itemName, itemWhich, questionNum (ボタンのテキスト) を渡す
            intentanser.putExtra("itemName", intent.getStringExtra("itemName"))
            intentanser.putExtra("itemWhich", intent.getStringExtra("itemWhich"))
            intentanser.putExtra("questionNum", buttonText)  // ボタンのテキストを渡す
            startActivity(intentanser)
        }
    }
}
