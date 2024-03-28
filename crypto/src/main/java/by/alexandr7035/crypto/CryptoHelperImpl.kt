package by.alexandr7035.crypto

import org.kethereum.bip39.model.MnemonicWords
import org.kethereum.bip39.toKey
import org.kethereum.crypto.toCredentials
import org.kethereum.model.Credentials

class CryptoHelperImpl : CryptoHelper {
    override suspend fun generateCredentialsFromMnemonic(mnemonic: String): Credentials {
       return MnemonicWords(mnemonic).toKey(BIP44_PATH_ETHEREUM).keyPair.toCredentials()
    }

    private companion object {
        const val BIP44_PATH_ETHEREUM = "m/44'/60'/0'/0/0"
    }
}