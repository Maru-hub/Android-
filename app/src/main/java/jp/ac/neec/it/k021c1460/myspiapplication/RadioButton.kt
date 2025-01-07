package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Context
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

class RadioButton   {
    fun add(key: String, value: Any,RgOpt: RadioGroup,context: Context) {
        val RbOpt = RadioButton(context)
        RgOpt.addView(RbOpt)
        //文字のレイアウト
        val WC = LinearLayout.LayoutParams.WRAP_CONTENT
        val MC = LinearLayout.LayoutParams.MATCH_PARENT
        val LP = LinearLayout.LayoutParams(WC, MC)
        RbOpt.layoutParams = LP
        RbOpt.textSize = 18F
        RbOpt.setPadding(0, 16, 0, 16)
        RbOpt.text = value.toString()
    }
}