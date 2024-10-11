package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // 戻り値用の変数を初期値trueで用意
        var returnVal = true

        when(item.itemId){
            R.id.menuListfirst -> {
                val intent = Intent(this, FirstActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.menuListsecond -> {
                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.menuListthird -> {
                val intent = Intent(this, ThirdActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.menuListfourth -> {
                val intent = Intent(this, FourthActivity::class.java)
                startActivity(intent)
                finish()
            }
            else -> super.onOptionsItemSelected(item)
        }
        // 選択されたメニューが「戻る」の場合、アクティビティを終了。
        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options_menu_list, menu)
        return true
    }
}