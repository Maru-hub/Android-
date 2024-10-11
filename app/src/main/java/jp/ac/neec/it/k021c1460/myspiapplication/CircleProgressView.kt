package jp.ac.neec.it.k021c1460.myspiapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CircleProgressView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var paint: Paint = Paint()
    private var circleColor: Int = Color.GRAY
    private var progressColor: Int = Color.RED
    private var progress: Float = 0f

    init {
        paint.isAntiAlias = true
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate() // 再描画
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 円を描く
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        paint.color = circleColor
        canvas.drawArc(rectF, 0f, 360f, false, paint)

        // 進捗を描く（時計回りで進行）
        paint.color = progressColor
        // 270度からスタートして時計回りに進行
        canvas.drawArc(rectF, 270f, progress * 360, true, paint)
    }
}