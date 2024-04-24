package com.example.votekt.domain.security.model

sealed class AuthenticationResult {
    object Success: AuthenticationResult()

    object Failure: AuthenticationResult()
}
