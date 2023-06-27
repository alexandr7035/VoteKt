package com.example.votekt.data.model

enum class TxStatus {
    REVERTED,
    PENDING,
    CONFIRMED,
    UNKNOWN;

//    companion object {
//        fun getStatusFromRaw(rawStatus: String) {
//            return when (rawStatus) {
//                "0x0" -> REVERTED,
//                    "0x1" ->
//            }
//        }
//    }
}