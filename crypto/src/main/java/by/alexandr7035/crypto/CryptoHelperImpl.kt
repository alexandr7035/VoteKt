package by.alexandr7035.crypto

import by.alexandr7035.utils.asBigInteger
import by.alexandr7035.utils.asEthereumAddressString
import cash.z.ecc.android.bip39.Mnemonics
import cash.z.ecc.android.bip39.toSeed
import pm.gnosis.crypto.KeyGenerator

class CryptoHelperImpl : CryptoHelper {
    override suspend fun generateCredentialsFromMnemonic(mnemonic: Mnemonics.MnemonicCode): Credentials {
        val mnemonicSeed = mnemonic.toSeed()
        val hdNode = KeyGenerator.masterNode(mnemonicSeed)
        val masterKey = hdNode.derive(BIP44_PATH_ETHEREUM)
        // a hard-coded first address here
        val firstChild = masterKey.deriveChild(0).keyPair

        return Credentials(
            address = firstChild.address.asEthereumAddressString(),
            privateKey = firstChild.privKey,
        )
    }

    private companion object {
        const val BIP44_PATH_ETHEREUM = "m/44'/60'/0'/0"
    }
}