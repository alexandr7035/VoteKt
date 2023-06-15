package com.example.votekt.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.data.VoterAddress
import com.example.votekt.data.Web3Repository
import com.example.votekt.data.impl.Web3RepositoryImpl
import com.example.votekt.ui.core.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VotingViewModel : ViewModel() {
    // TODO di
    private var web3Repository: Web3Repository = Web3RepositoryImpl()

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