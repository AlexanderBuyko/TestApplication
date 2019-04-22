package com.example.myapplication.api.models.LoginData

import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("access_token")
    var access_token: String,
    @SerializedName("token_type")
    var token_type: String,
    @SerializedName("expires_in")
    var expires_in: Int,
    @SerializedName("scope")
    var scope: String,
    @SerializedName("created_at")
    var created_at: Int
)
