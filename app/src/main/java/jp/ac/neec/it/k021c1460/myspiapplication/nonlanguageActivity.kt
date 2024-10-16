package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class nonlanguageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nonlanguage)

        //ListViewオブジェクトを取得。
        val lvGame = findViewById<ListView>(R.id.lvNonLanguage)
        //リストビューに表示するリストデータを作成。
        var nonlanguageList = mutableListOf(
            "料金割引", "確率", "速さと距離",
            "代金精算", "順列・組み合わせ")
        //アダプタオブジェクトを生成。
        val adapter = ArrayAdapter(
            this@nonlanguageActivity, android.R.layout.simple_list_item_1,
            nonlanguageList
        )
        //リストビューにアダプタオブジェクトを設定。
        lvGame.adapter = adapter
        //リストビューにリスナを設定。
        lvGame.onItemClickListener = ListItemClickListener()
        // ホームボタンであるButtonオブジェクトを取得
        val btBack = findViewById<Button>(R.id.btHome1)
        // リスナクラスのインスタンスを生成
        val listener = HelloListener()
        // 最初の画面に戻るボタンにリスナを設定
        btBack.setOnClickListener(listener)

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

    private inner class ListItemClickListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val item = parent?.getItemAtPosition(position)
            val intent_select =  Intent(this@nonlanguageActivity, SelectActivity1::class.java)
            startActivity(intent_select)
        }
    }

    //戻るボタンをタップした時の処理。
    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {
            when(view.id){
                R.id.btHome1 -> {
                    finish()
                }
            }
        }
    }
}