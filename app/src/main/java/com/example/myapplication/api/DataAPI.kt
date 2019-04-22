package com.example.myapplication.api


import com.example.myapplication.api.models.Jog.Jog
import com.example.myapplication.api.models.Jog.JogList
import com.example.myapplication.api.models.Response
import io.reactivex.Observable
import retrofit2.http.*

interface DataAPI {

    companion object {
        var accessToken: String = ""
    }

    @GET("data/sync")
    fun getJogs(@Header("Authorization") authString: String): Observable<Response<JogList>>

    @FormUrlEncoded
    @POST("data/jog")
    fun createJog(
        @Header("Authorization") authString: String,
        @Field("date") date: String,
        @Field("time") time: Int,
        @Field("distance") distance: Float
    ): Observable<Response<Jog>>

    @FormUrlEncoded
    @PUT("data/jog")
    fun updateJog(
        @Header("Authorization") authString: String,
        @Field("jog_id") jogId: Int,
        @Field("user_id") userId: String,
        @Field("date") date: String,
        @Field("time") time: Int,
        @Field("distance") distance: Float
    ): Observable<Response<Jog>>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "data/jog", hasBody = true)
    fun deleteJog(
        @Header("Authorization") authString: String,
        @Field("jog_id") jogId: Int,
        @Field("user_id") userId: String
    ): Observable<Response<String>>
}