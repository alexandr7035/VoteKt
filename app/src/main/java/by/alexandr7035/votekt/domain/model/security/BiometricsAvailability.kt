package by.alexandr7035.votekt.domain.model.security

sealed class BiometricsAvailability {
    object Checking : BiometricsAvailability()
    object Available : BiometricsAvailability()
    object NotAvailable : BiometricsAvailability()
    object NotEnabled : BiometricsAvailability()
}
