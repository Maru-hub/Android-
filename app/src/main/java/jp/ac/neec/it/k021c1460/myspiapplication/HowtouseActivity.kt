package jp.ac.neec.it.k021c1460.myspiapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton

class HowtouseActivity : AppCompatActivity() {
    private lateinit var explanationTextViews: List<TextView>
    private lateinit var expandButtons: List<MaterialButton>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_howtouse)

        // 各説明用のTextViewを取得
        explanationTextViews = listOf(
            findViewById(R.id.explanation_1),
            findViewById(R.id.explanation_2),
            findViewById(R.id.explanation_3),
            findViewById(R.id.explanation_4)
        )

        // 各ボタンを取得
        expandButtons = listOf(
            findViewById(R.id.expand_button_1),
            findViewById(R.id.expand_button_2),
            findViewById(R.id.expand_button_3),
            findViewById(R.id.expand_button_4)
        )

        // 各ボタンにクリックリスナーを設定
        expandButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                toggleExplanation(index)
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun toggleExplanation(index: Int) {
        explanationTextViews.forEachIndexed { i, textView ->
            if (i == index) {
                // 選択された説明をトグル
                val isVisible = textView.visibility == View.VISIBLE
                textView.visibility = if (isVisible) View.GONE else View.VISIBLE
                expandButtons[i].text = if (isVisible) "▼" else "▲"
            } else {
                // 他の説明は非表示にし、ボタンを「▼」に戻す
                textView.visibility = View.GONE
                expandButtons[i].text = "▼"
            }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options_menu_list, menu)
        return true
    }
}