package com.example.bluetoothlowenergytests.models

import com.example.bluetoothlowenergytests.models.api.ApiClient
import com.example.bluetoothlowenergytests.models.dto.cin.CinDto
import com.example.bluetoothlowenergytests.models.dto.cin.MCinDto
import com.example.bluetoothlowenergytests.models.dto.ResList
import com.example.bluetoothlowenergytests.models.dto.ae.AeDto
import com.example.bluetoothlowenergytests.models.dto.ae.MAeDto
import com.example.bluetoothlowenergytests.models.dto.cnt.CntDto
import com.example.bluetoothlowenergytests.models.dto.cnt.MCntDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResRepository {

    // GET list
    fun getList(onResult: (isSuccess: Boolean, response: List<String>?) -> Unit) {
        ApiClient.instance.getResList().enqueue(object : Callback<ResList> {
            override fun onResponse(call: Call<ResList>?, response: Response<ResList>?) {
                if (response != null && response.isSuccessful)
                    onResult(true, response.body()!!.list)
                else
                    onResult(false, null)
            }

            override fun onFailure(call: Call<ResList>?, t: Throwable?) {
                onResult(false, null)
            }

        })
    }

    fun getInstance(url: String, onResult: (isSuccess: Boolean, response: CinDto?) -> Unit) {
        ApiClient.instance.getInstance(url).enqueue(object : Callback<MCinDto> {
            override fun onResponse(call: Call<MCinDto>?, response: Response<MCinDto>?) {
                if (response != null && response.isSuccessful)
                    onResult(true, response.body()!!.cin)
                else
                    onResult(false, null)
            }

            override fun onFailure(call: Call<MCinDto>?, t: Throwable?) {
                onResult(false, null)
            }

        })
    }

    fun addApp(name: String, onResult: (isSuccess: Boolean) -> Unit) {
        val body = MAeDto(AeDto(name, "pt.ipleiria.$name", false))
        ApiClient.instance.addApp(body).enqueue(object : Callback<MAeDto> {
            override fun onResponse(call: Call<MAeDto>?, response: Response<MAeDto>?) {
                if (response != null && response.isSuccessful)
                    onResult(true)
                else
                    onResult(false)
            }

            override fun onFailure(call: Call<MAeDto>?, t: Throwable?) {
                onResult(false)
            }
        })
    }

    fun addContainer(name: String, url: String, onResult: (isSuccess: Boolean) -> Unit) {
        val body = MCntDto(CntDto(name))
        ApiClient.instance.addContainer(url, body).enqueue(object : Callback<MCntDto> {
            override fun onResponse(call: Call<MCntDto>?, response: Response<MCntDto>?) {
                if (response != null && response.isSuccessful)
                    onResult(true)
                else
                    onResult(false)
            }

            override fun onFailure(call: Call<MCntDto>?, t: Throwable?) {
                onResult(false)
            }
        })
    }

    fun addInstance(name: String, url: String, onResult: (isSuccess: Boolean) -> Unit) {
        val body = MCinDto(CinDto("text/plain:0", name))
        ApiClient.instance.addInstance(url, body).enqueue(object : Callback<MCinDto> {
            override fun onResponse(call: Call<MCinDto>?, response: Response<MCinDto>?) {
                if (response != null && response.isSuccessful)
                    onResult(true)
                else
                    onResult(false)
            }

            override fun onFailure(call: Call<MCinDto>?, t: Throwable?) {
                onResult(false)
            }
        })
    }

    fun removeResource(url: String, onResult: (isSuccess: Boolean) -> Unit) {
        ApiClient.instance.removeResource(url).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>?, response: Response<Unit>?) {
                if (response != null && response.isSuccessful)
                    onResult(true)
                else
                    onResult(false)
            }

            override fun onFailure(call: Call<Unit>?, t: Throwable?) {
                onResult(false)
            }
        })
    }


    companion object {
        private var INSTANCE: ResRepository? = null
        fun getInstance() = INSTANCE
                ?: ResRepository().also {
                    INSTANCE = it
                }
    }
}