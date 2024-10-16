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

class languageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)


        //ListViewオブジェクトを取得。
        val lvGame = findViewById<ListView>(R.id.lvLanguage)
        //リストビューに表示するリストデータを作成。
        val languageList = mutableListOf(
            "二語の関係", "熟語の成り立ち", "語句の意味",
            "文の並び替え", "空欄補充", "長文読解")
        //アダプタオブジェクトを生成。
        val adapter = ArrayAdapter(
            this@languageActivity, android.R.layout.simple_list_item_1,
            languageList
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
        override fun onItemClick(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            val item = parent.getItemAtPosition(position) as MutableMap<String,String>
            val itemName = item["name"]
            val intent_select =  Intent(this@languageActivity,SelectActivity::class.java)
            intent_select.putExtra("itemName",itemName)
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