package com.example.votekt.ui.votings_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.votekt.data.Web3Repository
import kotlinx.coroutines.launch

class CreateProposalViewModel(private val web3Repository: Web3Repository) : ViewModel() {
    fun createProposal(title: String, description: String) {
        viewModelScope.launch {
            web3Repository.createProposal(
                title = title,
                description = description,
            )
        }
    }
}