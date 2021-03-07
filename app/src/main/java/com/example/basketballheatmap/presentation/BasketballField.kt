package com.example.basketballheatmap.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * Created by Batuhan Duvarci on 5.03.2021.
 */
class BasketballField : View {
    private lateinit var paint: Paint
    private val BORDER_COLOR = Color.BLACK
    private val BORDER_WIDTH = 8F

    constructor(context: Context?) : super(context){
        init(null)
    }

    constructor(context: Context?, attributeSet: AttributeSet) : super(context, attributeSet){
        init(attributeSet)
    }

    constructor(context: Context?, attributeSet: AttributeSet, defStyle : Int) : super(context, attributeSet, defStyle){
        init(attributeSet)
    }

    private fun init(attributeSet: AttributeSet?){
        paint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        setMeasuredDimension(measuredWidth, measuredWidth.times(0.93).toInt())
        canvas.apply {
            paint.color = BORDER_COLOR
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = BORDER_WIDTH
            //main frame
            drawRect(0F, 0F, measuredWidth.toFloat(), measuredWidth.times(0.93).toFloat(), paint)
            //inner frame
            drawRect(measuredWidth.times(0.65).toFloat(), 0F, measuredWidth.times(0.35).toFloat(), measuredHeight.times(0.40).toFloat(), paint)
            //middle circle
            drawCircle(measuredWidth.times(0.50).toFloat(), measuredHeight.toFloat(), measuredWidth.times(0.125).toFloat(), paint)
            //inner circle
            drawCircle(measuredWidth.times(0.50).toFloat(), measuredHeight.times(0.40).toFloat(), measuredWidth.times(0.125).toFloat(), paint)
            //inner lines
            drawLine(measuredWidth.times(0.10).toFloat(), 0F, measuredWidth.times(0.10).toFloat(), measuredHeight.times(0.20).toFloat(), paint)
            drawLine(measuredWidth.times(0.90).toFloat(), 0F, measuredWidth.times(0.90).toFloat(), measuredHeight.times(0.20).toFloat(), paint)
            //main arc
            drawArc(measuredWidth.times(0.10).toFloat(), -measuredHeight.times(0.25).toFloat(), measuredWidth.times(0.90).toFloat(), measuredHeight.times(0.60).toFloat(), 0F, 180F, false, paint)
            //basket
            drawRect(measuredWidth.times(0.45).toFloat(), measuredHeight.times(0.086).toFloat(), measuredWidth.times(0.55).toFloat(), measuredHeight.times(0.086).toFloat(), paint)
            drawCircle(2.43F, 5.919F, measuredWidth.times(0.010).toFloat(), paint)
        }
    }
}