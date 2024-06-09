package com.example.votekt.ui.utils.compositionlocal

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import com.example.votekt.ui.components.snackbar.SnackBarMode
import com.example.votekt.ui.components.snackbar.showResultSnackBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// App host coroutine scope may be passed here
// e.g. to preserve snackbar if user leaves screen where it triggered
data class ScopedSnackBarState(
    private val value: SnackbarHostState,
    private val coroutineScope: CoroutineScope,
) {
    fun show(message: String, snackBarMode: SnackBarMode = SnackBarMode.Neutral) {
        coroutineScope.launch {
            value.showResultSnackBar(message, snackBarMode)
        }
    }

    fun show(context: Context, @StringRes message: Int, snackBarMode: SnackBarMode = SnackBarMode.Neutral) {
        coroutineScope.launch {
            value.showResultSnackBar(context.getString(message), snackBarMode)
        }
    }
}

val LocalScopedSnackbarState = compositionLocalOf<ScopedSnackBarState> { error("No ScopedSnackBarState provided") }
