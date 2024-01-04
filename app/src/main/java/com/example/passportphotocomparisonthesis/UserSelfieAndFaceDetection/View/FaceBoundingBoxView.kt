package com.example.passportphotocomparisonthesis.UserSelfieAndFaceDetection.View

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class FaceBoundingBoxView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var faceBox: Rect? = null
    private val paint = Paint().apply {
        style = Paint.Style.STROKE
    }

    fun updateFaceBox(faceBox: Rect) {
        this.faceBox = faceBox
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        faceBox?.let { box ->
            paint.color = Color.GREEN
            canvas.drawRect(box, paint)
        }
    }
}
