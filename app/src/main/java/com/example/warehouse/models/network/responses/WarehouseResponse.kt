package com.example.warehouse.models.network.responses

import com.google.gson.annotations.SerializedName

data class WarehouseResponse(
    @SerializedName("name")
    var name : String,
    @SerializedName("address")
    var address : String,
    @SerializedName("uuid")
    var uuid : String,
    @SerializedName("pictureUrl")
    var pictureUrl : String
){
}