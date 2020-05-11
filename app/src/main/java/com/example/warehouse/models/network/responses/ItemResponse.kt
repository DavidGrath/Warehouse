package com.example.warehouse.models.network.responses

import com.google.gson.annotations.SerializedName

data class ItemResponse(
    @SerializedName("uuid")
    val uuid : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("quantity")
    val quantity : Int,
    @SerializedName("unitPrice")
    val unitPrice : Float,
    @SerializedName("currencyCode")
    val currencyCode : String,
    @SerializedName("pictureUrl")
    val pictureUrl : String
) {
}