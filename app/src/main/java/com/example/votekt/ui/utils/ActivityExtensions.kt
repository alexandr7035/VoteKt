package com.example.votekt.ui.utils

import android.app.Activity
import android.view.WindowManager

fun Activity.lockScreenshots() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_SECURE,
        WindowManager.LayoutParams.FLAG_SECURE
    )
}

fun Activity.unlockScreenshots() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
}
