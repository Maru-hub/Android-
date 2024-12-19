package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Context
import androidx.appcompat.app.AlertDialog


class Dialog {
    var button: Boolean = false
    fun show(context: Context): Boolean {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("確認")
            .setMessage("模擬試験を終了しますよろしいですか？")
            .setPositiveButton("はい") { _, _ ->
                button = true
            }
            .setNegativeButton("いいえ") { _, _ ->
                button = false
            }
            .setOnCancelListener {
                button = false
            }
            .show()
        return button
    }
}