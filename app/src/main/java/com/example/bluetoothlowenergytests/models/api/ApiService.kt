package com.example.bluetoothlowenergytests.models.api

import com.example.bluetoothlowenergytests.models.dto.cin.MCinDto
import com.example.bluetoothlowenergytests.models.dto.ResList
import com.example.bluetoothlowenergytests.models.dto.ae.AeDto
import com.example.bluetoothlowenergytests.models.dto.ae.MAeDto
import com.example.bluetoothlowenergytests.models.dto.cnt.MCntDto
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Headers(
        "Accept: application/json",
        "X-M2M-RI: 00001"
    )
    @GET("onem2m")
    fun getResList(@Query("fu") fu: String = "1", @Query("rty") rty: String = "5"): Call<ResList>

    @Headers(
        "Accept: application/json",
        "X-M2M-RI: 00001"
    )
    @GET
    fun getInstance(@Url url: String): Call<MCinDto>

    //add
    @Headers(
        "Content-Type: application/vnd.onem2mres+json; ty=2",
        "X-M2M-RI: 00003"
    )
    @POST("onem2m")
    fun addApp(@Body body: MAeDto): Call<MAeDto>

    @Headers(
        "Content-Type: application/vnd.onem2mres+json; ty=3",
        "X-M2M-RI: 0005"
    )
    @POST
    fun addContainer(@Url url: String, @Body body: MCntDto): Call<MCntDto>

    @Headers(
        "Content-Type: application/vnd.onem2mres+json; ty=4",
        "X-M2M-RI: 0006"
    )
    @POST
    fun addInstance(@Url url: String, @Body body: MCinDto): Call<MCinDto>

    //remove
    @Headers(
        "X-M2M-RI: 00009"
    )
    @DELETE
    fun removeResource(@Url url: String): Call<Unit>
}