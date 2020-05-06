package com.example.warehouse.models.network.responses

import com.google.gson.annotations.SerializedName

data class RegisteredUser(
    @SerializedName("username")
    var username : String,
    @SerializedName("firstName")
    var firstName : String,
    @SerializedName("lastName")
    var lastName : String,
    @SerializedName("eMail")
    var eMail : String,
    @SerializedName("token")
    var token : String,
    @SerializedName("dp")
    var dp : String
) {
}