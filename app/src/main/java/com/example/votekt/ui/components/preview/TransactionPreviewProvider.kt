package com.example.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.domain.transactions.TransactionDomain
import com.example.votekt.domain.transactions.TransactionType
import com.example.votekt.domain.transactions.TransactionStatus
import java.math.BigInteger

class TransactionPreviewProvider: PreviewParameterProvider<TransactionDomain> {
    override val values: Sequence<TransactionDomain>
        get() = sequenceOf(
            TransactionDomain(
                status = TransactionStatus.MINED,
                type = TransactionType.CREATE_PROPOSAL,
                gasFee = Wei(BigInteger("1000000000000000")),
                dateSent = 0,
                hash = "0x12345"
            ),
            TransactionDomain(
                status = TransactionStatus.REVERTED,
                type = TransactionType.VOTE,
                gasFee = Wei(BigInteger("1000000000000000")),
                dateSent = 0,
                hash = "0x12345"
            ),
            TransactionDomain(
                status = TransactionStatus.PENDING,
                type = TransactionType.VOTE,
                gasFee = null,
                dateSent = 0,
                hash = "0x12345"
            ),
        )
}