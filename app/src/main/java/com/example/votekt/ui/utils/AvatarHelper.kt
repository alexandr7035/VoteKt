package com.example.votekt.ui.utils

object AvatarHelper {
    fun getAvatarUrl(identifier: String): String {
        return "${API}${identifier}"
    }

    private const val API = "https://api.dicebear.com/6.x/identicon/svg?seed="
}