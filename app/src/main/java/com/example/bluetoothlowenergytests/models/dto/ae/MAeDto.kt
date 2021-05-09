package com.example.bluetoothlowenergytests.models.dto.ae

import com.google.gson.annotations.SerializedName

data class MAeDto(
    @SerializedName("m2m:ae") val ae: AeDto
)