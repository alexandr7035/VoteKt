package com.example.votekt.ui.box_contract

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
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
        val url = "https://goerli.infura.io/v3/${BuildConfig.INFURA_API_KEY}"
        web3j = Web3j.build(HttpService(url))
    }

    val balanceState = MutableStateFlow("0")
    val netInfoState = MutableStateFlow("-")

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

            while (true) {
                val res = web3j.ethBlockNumber().send()
                netInfoState.value = res.blockNumber.toString()

                delay(5 * 1000)
            }
        }
    }
}