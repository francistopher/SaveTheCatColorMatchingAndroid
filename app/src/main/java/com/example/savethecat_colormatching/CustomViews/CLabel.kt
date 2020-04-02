package com.example.savethecat_colormatching.CustomViews

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.*
import android.util.Log
import android.util.TypedValue
import android.view.ViewPropertyAnimator
import android.widget.AbsoluteLayout
import android.widget.TextView
import android.widget.AbsoluteLayout.LayoutParams
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.example.savethecat_colormatching.MainActivity

class CLabel(textView: TextView, parentLayout: AbsoluteLayout, params:LayoutParams) {

    private var isInverted:Boolean = false

    private var originalParams:LayoutParams? = null
    private var shrunkParams:LayoutParams? = null

    private var textView:TextView? = null

    init {
        this.textView = textView
        this.textView!!.layoutParams = params
        parentLayout.addView(this.textView)
        setOriginalParams(params = params)
        setShrunkParams()
        setStyle()
    }

    fun getThis():TextView {
        return textView!!
    }

    private fun setOriginalParams(params:LayoutParams) {
        originalParams = params
    }

    private fun setShrunkParams() {
        shrunkParams = AbsoluteLayout.LayoutParams(originalParams!!.x / 2, originalParams!!.y / 2, 1, 1)
    }

    fun shrunk() {
        textView!!.layoutParams = shrunkParams!!
    }

    private var fadeAnimator:ViewPropertyAnimator? = null
    private var fadeAnimatorIsRunning:Boolean = false
    fun fade(In:Boolean, Out:Boolean, Duration:Float, Delay:Float) {
        if (fadeAnimator != null) {
            if (fadeAnimatorIsRunning) {
                fadeAnimator!!.cancel()
                fadeAnimatorIsRunning = false
                fadeAnimator = null
            }
        }
        Log.i("Animation", "RUNNING")
         if (In) {
             fadeAnimator = textView!!.animate().alpha(1.0f)
             fadeAnimator!!.interpolator = FastOutSlowInInterpolator()

        }
        if (Out and !In) {
            fadeAnimator = textView!!.animate().alpha(0.0f)
            fadeAnimator!!.interpolator = LinearOutSlowInInterpolator()
        }
        fadeAnimator!!.startDelay = (1000.0f * Delay).toLong()
        fadeAnimator!!.duration = (1000.0f * Duration).toLong()


        fadeAnimator!!.withStartAction {
            fadeAnimatorIsRunning = true
        }
        fadeAnimator!!.withEndAction {
            if (In and Out) {
                this.fade(In = false, Out = true, Duration = Duration, Delay = 0.0f)
            } else {
                fadeAnimator!!.cancel()
                fadeAnimatorIsRunning = false
                fadeAnimator = null
            }
        }
        if (!fadeAnimatorIsRunning) {
            fadeAnimator!!.start()
        }
    }

    fun setText(text:String) {
        textView!!.text = text
    }

    fun getOriginalParams():LayoutParams {
        return originalParams!!
    }

    fun setTextSize(size:Float) {
        textView!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    private var shape:GradientDrawable? = null
    fun setCornerRadiusAndBorderWidth(radius:Int, borderWidth:Int) {
        shape = null
        shape = GradientDrawable()
        shape!!.shape = RECTANGLE
        shape!!.setColor((textView!!.background as ColorDrawable).color)
        if (borderWidth > 0) {
            shape!!.setStroke(borderWidth, Color.BLUE)
        }
        shape!!.cornerRadius = radius.toFloat()
        textView!!.setBackgroundDrawable(shape)
    }

    fun setStyle() {
        fun lightDominant() {
            textView!!.setBackgroundColor(Color.BLACK)
            textView!!.setTextColor(Color.WHITE)
        }

        fun darkDominant() {
            textView!!.setBackgroundColor(Color.WHITE)
            textView!!.setTextColor(Color.BLACK)
        }

        if (MainActivity.isThemeDark) {
            if (isInverted) {
                lightDominant()
            } else {
                darkDominant()
            }
        } else {
            if (isInverted) {
                darkDominant()
            } else {
                lightDominant()
            }
        }
    }

}