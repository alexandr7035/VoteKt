package by.alexandr7035.crypto

import org.kethereum.model.Credentials

interface CryptoHelper {
    suspend fun generateCredentialsFromMnemonic(mnemonic: String): Credentials
}
