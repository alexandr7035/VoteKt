package by.alexandr7035.crypto

import cash.z.ecc.android.bip39.Mnemonics.MnemonicCode

interface CryptoHelper {
    suspend fun generateCredentialsFromMnemonic(mnemonic: MnemonicCode): Credentials
}