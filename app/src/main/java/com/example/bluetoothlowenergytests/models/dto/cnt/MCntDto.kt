package com.example.bluetoothlowenergytests.models.dto.cnt

import com.google.gson.annotations.SerializedName

data class MCntDto(
    @SerializedName("m2m:cnt") val cnt: CntDto
)