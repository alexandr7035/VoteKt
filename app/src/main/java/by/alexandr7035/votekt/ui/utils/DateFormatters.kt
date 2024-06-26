package by.alexandr7035.votekt.ui.utils

import android.content.Context
import by.alexandr7035.votekt.R
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Suppress("MagicNumber")
object DateFormatters {
    fun formatRemainingTime(time: Long, context: Context): String {
        val currentTime = System.currentTimeMillis()
        require(time >= currentTime) {
            "Current time is greater than time"
        }
        val duration = (time - currentTime).toDuration(DurationUnit.MILLISECONDS)

        val days = duration.inWholeDays.toInt()
        val hours = (duration.inWholeHours % 24).toInt()
        val minutes = (duration.inWholeMinutes % 60).toInt()
        val seconds = (duration.inWholeSeconds % 60).toInt()

        return when {
            days > 0 -> context.resources.getQuantityString(R.plurals.day_plural, days, days)
            hours > 0 -> context.resources.getQuantityString(R.plurals.hour_plural, hours, hours)
            minutes > 0 -> context.resources.getQuantityString(R.plurals.minute_plural, minutes, minutes)
            else -> context.resources.getQuantityString(R.plurals.second_plural, seconds, seconds)
        }
    }

    fun formatDurationTime(duration: Duration, context: Context): String {
        val days = duration.inWholeDays.toInt()
        val hours = (duration.inWholeHours % 24).toInt()
        val minutes = (duration.inWholeMinutes % 60).toInt()
        val seconds = (duration.inWholeSeconds % 60).toInt()

        return when {
            days > 0 -> context.resources.getQuantityString(R.plurals.day_plural, days, days)
            hours > 0 -> context.resources.getQuantityString(R.plurals.hour_plural, hours, hours)
            minutes > 0 -> context.resources.getQuantityString(R.plurals.minute_plural, minutes, minutes)
            else -> context.resources.getQuantityString(R.plurals.second_plural, seconds, seconds)
        }
    }
}
