package by.alexandr7035.votekt.domain.security.model

sealed class AuthenticationResult {
    object Success : AuthenticationResult()

    object Failure : AuthenticationResult()
}
