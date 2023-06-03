package com.example.votekt.ui.box_contract

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.BuildConfig
import com.example.votekt.contracts.Box
import com.example.votekt.ui.SimpleState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.web3j.crypto.Bip44WalletUtils
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.TransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.RoundingMode

class BoxViewModel : ViewModel() {

    private var web3j: Web3j = Web3j.build(HttpService(BuildConfig.ETH_NODE_URL))
    private var contract: Box
    private var txManager: TransactionManager

    init {
        Log.d("DEBUG_TAG", "Mnemonic: ${BuildConfig.TEST_MNEMONIC.split(" ").size} words")
        val credentials = Bip44WalletUtils.loadBip44Credentials("", BuildConfig.TEST_MNEMONIC)
        Log.d("DEBUG_TAG", "KEY PAIR for ${credentials.address} created")

        txManager = RawTransactionManager(web3j, credentials)
        contract = Box.load(BuildConfig.CONTRACT_ADDRESS, web3j, txManager, DefaultGasProvider())
    }

    val balanceState = MutableStateFlow("...")
    val netInfoState = MutableStateFlow("-")

    val boxState = MutableStateFlow(SimpleState(lastValue = 0L, isLoading = true))

    fun loadBalance() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = web3j.ethGetBalance(
                BuildConfig.TEST_ADDRESS, DefaultBlockParameterName.LATEST
            ).send()

            // Convert balance to Ether
            val etherValue = Convert.fromWei(res.balance.toString(), Convert.Unit.ETHER).toString()

            // Round the Ether value to 7 decimal places
            val roundedValue = BigDecimal(etherValue).setScale(7, RoundingMode.HALF_UP)

            val formattedBalance = String.format("%.7f", roundedValue)

            balanceState.value = formattedBalance
        }
    }


    fun loadNetInfo() {
        viewModelScope.launch(Dispatchers.IO) {

            while (isActive) {
                val res = web3j.ethBlockNumber().send()
                netInfoState.value = res.blockNumber.toString()

                delay(5_000)
            }
        }
    }

    fun readBox() {
        viewModelScope.launch(Dispatchers.IO) {
            val oldState = boxState.value.copy(isLoading = true)
            boxState.value = oldState

            val boxValue = contract.retrieve().send()
            Log.d("DEBUG_TAG", "READ box value: ${boxValue}")

            // TODO
            boxState.value = SimpleState(lastValue = boxValue.toLong(), isLoading = false)
        }
    }

    fun updateBox(newValue: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val oldState = boxState.value.copy(isLoading = true)
            boxState.value = oldState

            // TODO updating box
            delay(3000)

            readBox()
        }
    }
}