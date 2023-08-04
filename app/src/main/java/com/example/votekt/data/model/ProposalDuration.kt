package com.example.votekt.data.model

enum class ProposalDuration {
    DURATION_24_HOURS,
    DURATION_7_DAYS,
    DURATION_30_DAYS;

    fun getDurationInMills(): Long {
        return when (this) {
            DURATION_24_HOURS -> 24 * 3600_000L
            DURATION_7_DAYS -> 24 * 7 * 3600_000L
            DURATION_30_DAYS -> 24 * 30 * 3600_000L
        }
    }

    fun getDurationInString(): String {
        return when (this) {
            DURATION_24_HOURS -> "24 hours"
            DURATION_7_DAYS -> "7 days"
            DURATION_30_DAYS -> "30 days"
        }
    }
}