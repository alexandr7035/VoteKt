package com.example.votekt.ui.box_contract

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.BuildConfig
import com.example.votekt.ui.SimpleState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.RoundingMode

class BoxViewModel : ViewModel() {

    private var web3j: Web3j

    init {
        web3j = Web3j.build(HttpService(BuildConfig.ETH_NODE_URL))
    }

    val balanceState = MutableStateFlow("0")
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
            delay(3000)

            // TODO
            boxState.value = SimpleState(lastValue = 333, isLoading = false)
        }
    }

    fun updateBox(newValue: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val oldState = boxState.value.copy(isLoading = true)
            boxState.value = oldState

            delay(3000)

            // TODO
            boxState.value = SimpleState(lastValue = newValue, isLoading = false)
        }
    }
}