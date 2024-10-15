package jp.ac.neec.it.k021c1460.myspiapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button

class NextQuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_next_question)

        // ホームボタンであるButtonオブジェクトを取得
        val btBack = findViewById<Button>(R.id.bt_back)
        val btNext = findViewById<Button>(R.id.bt_next)
        // リスナクラスのインスタンスを生成
        val listener = HelloListener()
        // 最初の画面に戻るボタンにリスナを設定
        btBack.setOnClickListener(listener)
        btNext.setOnClickListener(listener)

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
    //戻るボタンをタップした時の処理。
    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {
            when(view.id){
                R.id.bt_back -> {
                    finish()
                }
                //R.id.bt_next -> {
                    //val intent2examMainActivity= Intent(this@examMainActivity, examSabActivity::class.java)
                    //startActivity(intent2examMainActivity)
                //}
            }
        }
    }
}