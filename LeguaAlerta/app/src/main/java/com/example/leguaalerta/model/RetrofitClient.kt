package com.example.leguaalerta.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val URL_BASE = "https://x8ki-letl-twmt.n7.xano.io/api:2v8EZ8MJ/"

    val servicio: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
