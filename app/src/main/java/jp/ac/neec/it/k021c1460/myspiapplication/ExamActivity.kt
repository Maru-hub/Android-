package jp.ac.neec.it.k021c1460.myspiapplication

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class ExamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam)

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

        // Spinner の参照を取得
        val spinner: Spinner = findViewById(R.id.spinner)

        // データのリストを作成
        val items = listOf("模擬試験1", "模擬試験2", "模擬試験3")

        // ArrayAdapter を作成
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        // スピナーのスタイルを設定 (ドロップダウンのスタイル)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // スピナーにアダプターをセット
        spinner.adapter = adapter

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
                R.id.btHome1 -> {
                    finish()
                }
                //模試開始ボタン
                R.id.button -> {
                    // 確認ダイアログを表示
                    showConfirmationDialog()
                }
            }
        }
    }

    private fun showConfirmationDialog() {
        // Spinner の参照を取得
        val spinner: Spinner = findViewById(R.id.spinner)
        val selectedItem = spinner.selectedItem.toString() // アイテムの文字列を取得

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("確認")
            .setMessage("$selectedItem を始めます。よろしいですか？\n"+"※模擬試験は次の問題に進んだら\n前の問題に戻ることはできません。\n" +
                    "※問題数は30問あります。\n一回終了すると最初からになってしまいます。")
            .setPositiveButton("はい") { _, _ ->
                // 試験開始画面に遷移
                if(User().LoginCheck(this)){
                    val intent2Teststart = Intent(this@ExamActivity, ExamMainActivity::class.java)
                    intent2Teststart.putExtra("examName",selectedItem)
                    startActivity(intent2Teststart)
                    finish()
                }
            }
            .setNegativeButton("いいえ", null) // 何もしない
            .show()
    }

}