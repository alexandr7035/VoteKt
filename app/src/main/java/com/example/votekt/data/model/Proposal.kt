package com.example.votekt.data.model

data class Proposal(
    val id: String,
    val title: String,
    val description: String,
    val votesFor: Int,
    val votesAgainst: Int
) {

    companion object {
        fun mock(): Proposal {
            return Proposal(
                id = "0",
                title = "My awesome Proposal",
                description = "My awesome description. Lorem ipsum lorem ipsum. My awesome description. Lorem ipsum lorem ipsum.",
                votesFor = 10,
                votesAgainst = 25,
            )
        }
    }

}
