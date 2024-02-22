package com.example.votekt.ui.components.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import by.alexandr7035.ethereum.model.Wei
import com.example.votekt.data.model.Transaction
import com.example.votekt.data.model.TransactionType
import com.example.votekt.data.web3_core.transactions.TxStatus
import java.math.BigInteger

class TransactionPreviewProvider: PreviewParameterProvider<Transaction> {
    override val values: Sequence<Transaction>
        get() = sequenceOf(
            Transaction(
                status = TxStatus.MINED,
                type = TransactionType.CREATE_PROPOSAL,
                gasFee = Wei(BigInteger("1000000000000000")),
                dateSent = 0,
                hash = "0x12345"
            ),
            Transaction(
                status = TxStatus.REVERTED,
                type = TransactionType.VOTE,
                gasFee = Wei(BigInteger("1000000000000000")),
                dateSent = 0,
                hash = "0x12345"
            ),
            Transaction(
                status = TxStatus.PENDING,
                type = TransactionType.VOTE,
                gasFee = null,
                dateSent = 0,
                hash = "0x12345"
            ),
        )
}