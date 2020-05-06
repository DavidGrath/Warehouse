package com.example.warehouse.models.network.requests

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username")
    var username : String,
    @SerializedName("first_name")
    var firstName : String,
    @SerializedName("last_name")
    var lastName : String,
    @SerializedName("e_mail")
    var eMail : String,
    @SerializedName("password")
    var password : String
) {
}