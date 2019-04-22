package com.example.myapplication.api

import com.example.myapplication.api.models.LoginData.LoginData
import com.example.myapplication.api.models.Response
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthAPI {
    @POST("auth/uuidLogin")
    fun logIn(@Query("uuid") uuid: String): Observable<Response<LoginData>>
}