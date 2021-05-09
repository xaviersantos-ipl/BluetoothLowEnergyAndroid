package com.example.bluetoothlowenergytests.models.dto

import com.google.gson.annotations.SerializedName

data class ResList(
        @SerializedName("m2m:uril") val list: List<String>
)