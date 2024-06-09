package by.alexandr7035.votekt.domain.security.model

sealed class BiometricsAvailability {
    object Checking : BiometricsAvailability()
    object Available : BiometricsAvailability()
    object NotAvailable : BiometricsAvailability()
    object NotEnabled : BiometricsAvailability()
}
