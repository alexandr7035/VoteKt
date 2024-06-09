package by.alexandr7035.votekt.ui.feature.onboarding.model

import android.content.Context
import by.alexandr7035.votekt.R

data class WelcomePageUi(
    val title: String,
    val description: String,
    val imgRes: Int
) {
    companion object {
        fun getPages(context: Context): List<WelcomePageUi> {
            return listOf(
                WelcomePageUi(
                    title = context.getString(R.string.welcome_title_1),
                    description = context.getString(R.string.welcome_desc_1),
                    imgRes = R.drawable.thumb_up
                ),

                WelcomePageUi(
                    title = context.getString(R.string.welcome_title_2),
                    description = context.getString(R.string.welcome_desc_2),
                    imgRes = R.drawable.ic_key
                ),

                WelcomePageUi(
                    title = context.getString(R.string.welcome_title_3),
                    description = context.getString(R.string.welcome_desc_3),
                    imgRes = R.drawable.ic_community
                ),
            )
        }
    }
}
