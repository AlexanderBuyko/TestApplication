package com.example.myapplication.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class APIBuilder {
    companion object {
        private var retrofit: Retrofit? = null


        fun instance(): Retrofit {
            if (retrofit == null) {
                val okhttpBuilder = OkHttpClient.Builder()

                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                okhttpBuilder.addInterceptor(logging)

                retrofit = Retrofit.Builder()
                    .baseUrl("https://jogtracker.herokuapp.com/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okhttpBuilder.build())
                    .build()
            }
            return retrofit!!
        }
    }



}