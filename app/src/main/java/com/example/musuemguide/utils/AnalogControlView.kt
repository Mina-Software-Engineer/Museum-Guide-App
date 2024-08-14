package com.example.musuemguide.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class AnalogControlView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint() // Paint object for drawing
    private val joystickRadius = 300f // Adjust the radius as needed
    private var knobX = width / 2F // Center X coordinate of the control
    private var knobY = height / 2F // Center Y coordinate of the control


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Custom drawing of the control (e.g., draw a circular shape)
        //canvas.drawCircle(centerX, centerY, radius, paint)

        val centerX = width / 2f
        val centerY = height / 2f

        // Draw the circular background for the joystick
        paint.color = Color.LTGRAY // Background color
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 7f
        canvas.drawCircle(centerX, centerY, joystickRadius, paint)


        // Draw the central knob of the joystick
        paint.color = Color.GRAY // Knob color
        paint.style = Paint.Style.FILL
        val knobRadius = joystickRadius / 3 // Adjust knob size relative to the background
        canvas.drawCircle(knobX, knobY, knobRadius, paint)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        knobX = w / 2F
        knobY = h / 2F
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val touchX = event.x
                val touchY = event.y

                // Calculate distance from the center of the joystick
                val distanceX = touchX - width / 2
                val distanceY = touchY - height / 2
                val distance =
                    sqrt((distanceX * distanceX + distanceY * distanceY).toDouble()).toFloat()

                // If the touch is within the joystick boundary
                if (distance <= joystickRadius) {
                    // Update knob position
                    knobX = touchX
                    knobY = touchY

                    // Ensure the view gets redrawn
                    invalidate()
                } else {
                    // Calculate knob position within the boundary based on angle
                    val angle = atan2(distanceY.toDouble(), distanceX.toDouble()).toFloat()
                    knobX = (width / 2 + (joystickRadius * cos(angle))).toFloat()
                    knobY = (height / 2 + (joystickRadius * sin(angle))).toFloat()

                    // Ensure the view gets redrawn
                    invalidate()
                }
            }

            MotionEvent.ACTION_UP -> {
                // Reset knob position to center when touch is released
                knobX = width / 2f
                knobY = height / 2f

                // Ensure the view gets redrawn
                invalidate()
            }
        }
        return true
    }


}
