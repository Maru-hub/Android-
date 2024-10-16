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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.firestore

class SelectActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_select1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets }

        val db = Firebase.firestore
        val itemName = intent.getStringExtra("itemName")
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        // ボタンを動的に生成して、TableLayoutに追加する
        val totalButtons = 10
        val buttonPlusNum = (totalButtons/3+1)*3
        val buttonsPerRow = 3

        for (i in 0 until totalButtons step buttonsPerRow) {
            val tableRow = TableRow(this)

            for (j in 0 until buttonsPerRow) {

                val button = Button(this)

                // リスナクラスのインスタンスを生成
                val listener = HelloListener()
                // 最初の画面に戻るボタンにリスナを設定
                button.setOnClickListener(listener)

                button.setBackgroundColor(Color.parseColor("#65BBE9"))
                val layoutParams = TableRow.LayoutParams(
                    0, 96, 0.3f
                ).apply { setMargins(8, 8, 8, 8) } // 左右に8dpのマージンを設定
                // ボタンにレイアウトパラメータをセット
                button.layoutParams = layoutParams

                if ((i+j)+1 == buttonPlusNum || (i+j)+1 == buttonPlusNum-1) {
                    button.text = ""
                    button.alpha=0f
                    button.isClickable = false
                }
                else {
                    button.text = "問題 ${(i + j) + 1}"  // ボタンに番号を付ける
                }

                tableRow.addView(button)
            }
            // TableRowをTableLayoutに追加
            tableLayout.addView(tableRow)
        }

        val partRef = db.collection("非言語").
        document("7vYnfiq5TYFD4Kj2v3BW").
        collection("組み合わせ")
        val query = partRef
        val countQuery = query.count()
        countQuery.get(AggregateSource.SERVER).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Count fetched successfully
                val snapshot = task.result
                val num = snapshot.count
                Log.d(TAG, "Count: ${num}")

            } else {
                Log.d(TAG, "Count failed: ", task.getException())
            }
        }

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

    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {
            val intentanser = Intent(this@SelectActivity1, AnswerActivity::class.java)
            startActivity(intentanser)
        }
    }
}
