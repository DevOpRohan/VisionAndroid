package org.tensorflow.lite.examples.objectdetection

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class DoubleTapView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private lateinit var gestureDetector: GestureDetector

    var onDoubleTapListener: (() -> Unit)? = null

    init {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                onDoubleTapListener?.invoke()
                return true
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}