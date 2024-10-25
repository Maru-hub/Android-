package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class nonlanguageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nonlanguage)

        val db = Firebase.firestore

        val docRef = db.collection("非言語").document("項目")
        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // ドキュメントデータを取得
                val data = documentSnapshot.data
                Log.d("TAG", "Document data: $data")

                // ListViewオブジェクトを取得。
                val lvGame = findViewById<ListView>(R.id.lvNonLanguage)

                val listData : MutableList<String> = mutableListOf()

                if (data != null) {
                    for ((key, value) in data) {
                        listData.add(value.toString())
                    }
                }

                Log.d("TAG", "list data: $listData")
                // アダプタオブジェクトを生成。
                val adapter = ArrayAdapter(this@nonlanguageActivity,
                    android.R.layout.simple_list_item_1,
                    listData // documentDataをそのまま渡す
                )

                lvGame.adapter = adapter // アダプタをListViewに設定

                //リストビューにリスナを設定。
                lvGame.onItemClickListener = ListItemClickListener()
            } else {
                Log.d("TAG", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting document", exception)
            }
        // ホームボタンであるButtonオブジェクトを取得
        val btBack = findViewById<Button>(R.id.bthome2)
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
            val item = parent.getItemAtPosition(position) as String
            val intent_select =  Intent(this@nonlanguageActivity,SelectActivity::class.java)
            intent_select.putExtra("itemName",item)
            intent_select.putExtra("itemWhich","非言語")
            startActivity(intent_select)
        }
    }

    //戻るボタンをタップした時の処理。
    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {
            when(view.id){
                R.id.bthome2-> {
                    finish()
                }
            }
        }
    }
}