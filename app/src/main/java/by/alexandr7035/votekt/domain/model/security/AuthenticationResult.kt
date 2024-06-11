package by.alexandr7035.votekt.domain.model.security

sealed class AuthenticationResult {
    object Success : AuthenticationResult()

    object Failure : AuthenticationResult()
}
