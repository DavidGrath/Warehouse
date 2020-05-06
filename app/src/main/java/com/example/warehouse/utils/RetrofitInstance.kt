package com.example.warehouse.utils

import com.example.warehouse.utils.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    val client = OkHttpClient.Builder()
        .writeTimeout(10, TimeUnit.SECONDS)
        .callTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        })
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)

        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val warehouseService = retrofit.create(WarehouseService::class.java)
}