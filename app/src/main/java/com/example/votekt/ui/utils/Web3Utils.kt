package com.example.votekt.ui.utils

import org.kethereum.model.Address

fun Address.prettify(): String {
    return this.hex.prettifyAddress()
}