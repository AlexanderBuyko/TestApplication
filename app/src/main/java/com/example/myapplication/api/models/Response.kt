package com.example.myapplication.api.models

import com.google.gson.annotations.SerializedName

data class Response<T>(
    @SerializedName("response")
    var data: T
)