package com.example.savethecat_colormatching.Characters

import android.widget.AbsoluteLayout
import android.widget.Button

class CatButtons {

    private var currentCatButtons:MutableList<CatButton>? = null

    init {
        currentCatButtons = mutableListOf()
    }

    private var catButton:CatButton? = null
    fun buildCatButton(button: Button, parentLayout: AbsoluteLayout,
                       params: AbsoluteLayout.LayoutParams, backgroundColor:Int): CatButton {
        catButton = CatButton(button=button, parentLayout=parentLayout, params=params,
            backgroundColor = backgroundColor)
        // If cat button is pressed fade out cat button
        currentCatButtons!!.add(catButton!!)
        return catButton!!
    }
}