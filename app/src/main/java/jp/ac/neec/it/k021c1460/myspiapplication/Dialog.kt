package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Context
import androidx.appcompat.app.AlertDialog
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Dialog {
    suspend fun show(context: Context): Boolean = suspendCoroutine { continuation ->
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setTitle("確認")
            .setMessage("模擬試験を終了しますよろしいですか？")
            .setPositiveButton("はい") { _, _ ->
                continuation.resume(true) // 「はい」を押した場合
            }
            .setNegativeButton("いいえ") { _, _ ->
                continuation.resume(false) // 「いいえ」を押した場合
            }
            .setOnCancelListener {
                continuation.resume(false) // キャンセルした場合
            }
            .show()
    }
}