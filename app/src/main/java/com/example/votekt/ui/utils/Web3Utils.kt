package com.example.votekt.ui.utils

import by.alexandr7035.ethereum.model.Address

fun Address.prettify(): String {
    return this.value.prettifyAddress()
}