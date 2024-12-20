package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 言語ボタンであるButtonオブジェクトを取得
        val btLanguage = findViewById<Button>(R.id.btlanguage)
        // リスナクラスのインスタンスを生成
        val listener = HelloListener()
        // ログインボタンをであるButtonオブジェクトを取得
        val btLogin = findViewById<Button>(R.id.btlogin)
        // ログインボタンにリスナを設定
        btLogin.setOnClickListener(listener)
        // 言語ボタンにリスナを設定
        btLanguage.setOnClickListener(listener)
        // 非言語ボタンであるButtonオブジェクトを取得
        val btNonlanguage = findViewById<Button>(R.id.btnonlanguage)
        // 非言語ボタンにリスナを設定
        btNonlanguage.setOnClickListener(listener)
        // 模試試験ボタンであるButtonオブジェクトを取得
        val btMocktest = findViewById<Button>(R.id.btmocktest)
        // 模試試験ボタンにリスナ設定
        btMocktest.setOnClickListener(listener)
        // 学習状況ボタンであるButtonオブジェクトを取得
        val btresult = findViewById<Button>(R.id.btresult)
        // 模試試験ボタンにリスナ設定
        btresult.setOnClickListener(listener)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options_menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuListfirst -> navigateToExplanation(0) // 「このアプリを使うにあたって」
            R.id.menuListsecond -> navigateToExplanation(1) // 「SPIについて」
            R.id.menuListthird -> navigateToExplanation(2) // 「能力検査の評価基準」
            R.id.menuListfourth -> navigateToExplanation(3) // 「テストの種類」
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToExplanation(index: Int) {
        val intent = Intent(this, HowtouseActivity::class.java)
        intent.putExtra("EXPLANATION_INDEX", index)
        startActivity(intent)
    }

    private inner class HelloListener : View.OnClickListener {
        override fun onClick(view: View) {
            when (view.id) {
                // ログインボタンの場合
                R.id.btlogin -> {
                    val intent2language = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent2language)
                }
                // 言語ボタンの場合
                R.id.btlanguage -> {
                    val intent2language = Intent(this@MainActivity, LanguageActivity::class.java)
                    startActivity(intent2language)
                }
                // 非言語ボタンの場合
                R.id.btnonlanguage -> {
                    val intent2nonlanguage = Intent(this@MainActivity, NonLanguageActivity::class.java)
                    startActivity(intent2nonlanguage)
                }
                // 模試試験ボタンの場合
                R.id.btmocktest -> {
                    val intent2mocktest = Intent(this@MainActivity, ExamActivity::class.java)
                    startActivity(intent2mocktest)
                }
                // 学習状況ボタンの場合
                R.id.btresult -> {
                    val intent2learn = Intent(this@MainActivity, LearnActivity::class.java)
                    startActivity(intent2learn)
                }
            }
        }
    }
}