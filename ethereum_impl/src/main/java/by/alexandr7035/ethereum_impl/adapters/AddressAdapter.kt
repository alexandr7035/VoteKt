package by.alexandr7035.ethereum_impl.adapters

import by.alexandr7035.ethereum.model.Address
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class AddressAdapter {
    @ToJson
    fun toJson(address: Address): String = address.value

    @FromJson
    fun fromJson(address: String): Address = Address(address)
}