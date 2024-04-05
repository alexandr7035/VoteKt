package com.example.votekt.domain.votings

enum class ProposalDuration {
    DURATION_24_HOURS,
    DURATION_7_DAYS,
    DURATION_30_DAYS;

    fun getDurationInDays(): Long {
        return when (this) {
            DURATION_24_HOURS -> 1
            DURATION_7_DAYS -> 7
            DURATION_30_DAYS -> 30
        }
    }

    fun getDurationInString(): String {
        return when (this) {
            DURATION_24_HOURS -> "24 hours"
            DURATION_7_DAYS -> "7 days"
            DURATION_30_DAYS -> "30 days"
        }
    }

    companion object {
        val default = DURATION_7_DAYS
    }
}