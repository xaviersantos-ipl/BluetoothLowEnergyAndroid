package com.example.bluetoothlowenergytests.models.api

import android.R.attr.password
import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import com.example.bluetoothlowenergytests.Constants.Companion.BASE_URL
import com.example.bluetoothlowenergytests.Constants.Companion.PASS
import com.example.bluetoothlowenergytests.Constants.Companion.REQUEST_TIMEOUT_DURATION
import com.example.bluetoothlowenergytests.Constants.Companion.USER
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiClient {

    val instance: ApiService = Retrofit.Builder().run {
        val gson = GsonBuilder()
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create()

        baseUrl(BASE_URL)
        addConverterFactory(GsonConverterFactory.create(gson))
        client(createRequestInterceptorClient())
        build()
    }.create(ApiService::class.java)


    private fun createRequestInterceptorClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val credential: String = Credentials.basic(USER, PASS)
            val original = chain.request()
            val request = original.newBuilder()
                    .addHeader("Authorization", credential)
                    .build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .connectTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                    .readTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                    .writeTimeout(REQUEST_TIMEOUT_DURATION.toLong(), TimeUnit.SECONDS)
                    .build()

    }
}