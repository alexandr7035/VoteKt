package by.alexandr7035.votekt.data.cache

object PrefKeys {
    const val ACCOUNT_ADDRESS_KEY = "account_address"
    const val ACCOUNT_MNEMONIC_PHRASE = "account_mnemonic_phrase"
    const val RECENT_BALANCE = "recent_balance"

    const val PIN_KEY = "PIN_KEY"
    const val PIN_SALT_KEY = "PIN_SALT"
    const val BIOMETRICS_ENCRYPTED_PIN_KEY = "BIOMETRICS_ENCRYPTED_PIN_KEY"
    const val BIOMETRICS_FLAG = "BIOMETRICS_LOCK_ENABLED"

    const val CONTRACT_CREATOR_ADDRESS = "contract.creator"
    const val CONTRACT_MAX_PROPOSALS = "contract.max_proposals"
    const val CONTRACT_MAX_PROPOSAL_TITLE_LENGTH = "contract.max_proposal_title_length"
    const val CONTRACT_MAX_DESCRIPTION_LENGTH = "contract.max_proposal_description_length"
    const val CONTRACT_CREATE_PROPOSAL_FEE = "contract.create_proposal_fee"
}
