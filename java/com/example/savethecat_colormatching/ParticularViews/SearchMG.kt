package com.example.savethecat_colormatching.ParticularViews

import android.animation.ValueAnimator
import android.content.Context
import android.widget.AbsoluteLayout
import android.widget.Button
import androidx.core.animation.doOnEnd
import com.daasuu.ei.Ease
import com.daasuu.ei.EasingInterpolator
import com.example.savethecat_colormatching.MainActivity
import com.example.savethecat_colormatching.R


class SearchMG(button: Button,
               parentLayout: AbsoluteLayout,
               params: AbsoluteLayout.LayoutParams) {

    private var buttonMG: Button? = null
    private var textButton: Button? = null
    private var searchContext: Context? = null
    private var originalParams: AbsoluteLayout.LayoutParams? = null
    private var parentLayout: AbsoluteLayout? = null
    private var targetParams: MutableMap<MGPosition, AbsoluteLayout.LayoutParams>? = mutableMapOf()

    init {
        setupMGButton(button)
        setupOriginalParams(params)
        setupParentLayout(parentLayout)
        setupTextButton()
        setupRotationAnimation()
        setStyle()
    }

    private fun setupMGButton(button: Button) {
        button.alpha = 0f
        buttonMG = button
        searchContext = button.context
    }

    private fun setupTextButton() {
        textButton = Button(searchContext)
        textButton!!.alpha = 0f
        textButton!!.layoutParams = originalParams!!
        parentLayout!!.addView(textButton!!)
    }

    private fun setupOriginalParams(params: AbsoluteLayout.LayoutParams) {
        originalParams = params
        buttonMG!!.layoutParams = params
        targetParams!![MGPosition.CENTER] = params
    }

    private fun setupParentLayout(layout: AbsoluteLayout) {
        parentLayout = layout
        layout.addView(buttonMG!!)
    }

    private var stoppingAnimation:Boolean = false
    fun stopAnimation() {
        stoppingAnimation = true
        fade(true)
    }

    /*
        Animation that rotates text around the magnifying glass
     */
    private var rotationAnimation: ValueAnimator? = null
    private fun setupRotationAnimation() {
        // If the rotation animation is running, cancel it
        if (rotationAnimation != null) {
            rotationAnimation!!.cancel()
        }
        // Move the text clockwise
        rotationAnimation = ValueAnimator.ofFloat(textButton!!.rotation, textButton!!.rotation + 90f)
        rotationAnimation!!.addUpdateListener {
            textButton!!.rotation = (it.animatedValue as Float)
        }
        // Setup the rotation animation text properties
        rotationAnimation!!.interpolator = EasingInterpolator(Ease.LINEAR)
        rotationAnimation!!.startDelay = 0
        rotationAnimation!!.duration = 1000
        rotationAnimation!!.doOnEnd {
            setupRotationAnimation()
            rotationAnimation!!.start()
        }
    }

    /*
        Update the transparency of the searching magnifying glass
     */
    private var fadeAnimator: ValueAnimator? = null
    private fun fade(out:Boolean) {
        // If the fading animation is running, cancel it
        if (fadeAnimator != null) {
            fadeAnimator!!.cancel()
        }
        // Fade the magnifying glass in or out
        fadeAnimator = if (out) {
            ValueAnimator.ofFloat(buttonMG!!.alpha, 0f)
        } else {
            ValueAnimator.ofFloat(buttonMG!!.alpha, 1f)
        }
        // Set the fading animation properties
        fadeAnimator!!.addUpdateListener {
            buttonMG!!.alpha = (it.animatedValue as Float)
            textButton!!.alpha = (it.animatedValue as Float)
        }
        fadeAnimator!!.duration = 1000
        fadeAnimator!!.start()
    }

    fun startSearchingAnimation() {
        if (MainActivity.isGooglePlayGameServicesAvailable && MainActivity.isInternetReachable) {
            fade(false)
            rotationAnimation!!.start()
        } else {
            displayFailureReason()
        }
    }

    private fun displayFailureReason() {
        if (!MainActivity.isInternetReachable) {
            MainActivity.gameNotification!!.displayNoInternet()
        }
        if (!MainActivity.isGooglePlayGameServicesAvailable) {
            MainActivity.gameNotification!!.displayNoGooglePlayGameServices()
        }
    }

    /*
        Update the style of the magnifying glass based off the
        theme of the operating system
     */
    fun setStyle() {
        fun lightDominant() {
            buttonMG!!.setBackgroundResource(R.drawable.lightmagnify)
            textButton!!.setBackgroundResource(R.drawable.darksearchingtext)
        }
        fun darkDominant() {
            buttonMG!!.setBackgroundResource(R.drawable.darkmagnify)
            textButton!!.setBackgroundResource(R.drawable.lightsearchingtext)
        }
        if (MainActivity.isThemeDark) {
            darkDominant()
        } else {
            lightDominant()
        }
    }
}