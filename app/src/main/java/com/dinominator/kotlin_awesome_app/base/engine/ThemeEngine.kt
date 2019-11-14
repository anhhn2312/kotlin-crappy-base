package com.dinominator.kotlin_awesome_app.base.engine

import com.dinominator.kotlin_awesome_app.R
import com.dinominator.kotlin_awesome_app.base.BaseActivity

object ThemeEngine {

    fun setTheme(activity: BaseActivity, theme: Int) {
        if (!hasTheme(activity)) {
            val style: Int = when (theme) {
                1 -> R.style.ThemeLight
                2 -> R.style.ThemeDark
                else -> R.style.ThemeLight
            }
            activity.setTheme(style)
        }
    }

    fun isLightTheme(theme: Int) = theme == R.style.ThemeLight

    private fun hasTheme(activity: BaseActivity) = false
}