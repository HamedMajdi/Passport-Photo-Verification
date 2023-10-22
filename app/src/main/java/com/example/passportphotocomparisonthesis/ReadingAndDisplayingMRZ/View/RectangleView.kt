package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.View

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class RectangleView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val paint = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    var blockFrame: Rect? = Rect(10, 20, 930,900)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        blockFrame?.let {
            canvas.drawRect(it, paint)
        }
    }
}