package com.example.myapplication.api.models.Jog

import com.google.gson.annotations.SerializedName

data class JogList  (
    @SerializedName("jogs")
    var jogs: ArrayList<Jog>
)

data class Jog(
    @SerializedName("id")
    var id: Int,
    @SerializedName("user_id")
    var user_id: String,
    @SerializedName("distance")
    var distance: Double,
    @SerializedName("time")
    var time: Long,
    @SerializedName("date")
    var date: String
)



