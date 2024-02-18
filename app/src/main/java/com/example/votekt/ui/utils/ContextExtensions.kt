package com.example.votekt.ui.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.copyToClipboard(
    clipLabel: String,
    text: String,
) {
    val clipBoard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    clipBoard?.setPrimaryClip(
        ClipData.newPlainText(
            clipLabel,
            text
        )
    )
}
