package com.example.votekt.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.data.VoterAddress
import com.example.votekt.data.Web3Repository
import com.example.votekt.ui.core.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VotingViewModel(private val web3Repository: Web3Repository) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val pr = web3Repository.getProposals()
            Log.d("DEBUG_TAG", "${pr}")
        }
    }

    // TODO reducer
    private val _votedAddressesUi = MutableStateFlow(
        value = ScreenState<List<VoterAddress>>(data = null, isLoading = true, error = null)
    )

    fun loadVotedAddresses() {
        viewModelScope.launch {
            val res = web3Repository.getVotedAddresses()
            _votedAddressesUi.value = _votedAddressesUi.value.copy(data = res, isLoading = false)
        }
    }

    fun getVotedAddressesObservable(): StateFlow<ScreenState<List<VoterAddress>>> {
        return _votedAddressesUi.asStateFlow()
    }
}