package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class mockexamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mockexam)

        // 試験開始ボタンであるButtonオブジェクトを取得
        val btTeststart = findViewById<Button>(R.id.button)
        // ホームボタンであるButtonオブジェクトを取得
        val btBack = findViewById<Button>(R.id.btHome1)
        // リスナクラスのインスタンスを生成
        val listener = HelloListener()
        // 最初の画面に戻るボタンにリスナを設定
        btBack.setOnClickListener(listener)
        // 試験開始ボタンにリスナ設定
        btTeststart.setOnClickListener(listener)
    }
    //戻るボタンをタップした時の処理。
    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {
            when(view.id){
                R.id.btHome1 -> {
                    finish()
                }
                R.id.button -> {
                    val intent2Teststart= Intent(this@mockexamActivity, examMainActivity::class.java)
                    startActivity(intent2Teststart)
                }
            }
        }
    }
}