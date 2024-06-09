package by.alexandr7035.ethereumimpl.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.kethereum.model.Address

class AddressAdapter {
    @ToJson
    fun toJson(address: Address): String = address.hex

    @FromJson
    fun fromJson(address: String): Address = Address(address)
}
