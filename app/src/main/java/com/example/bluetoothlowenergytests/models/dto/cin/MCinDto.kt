package com.example.bluetoothlowenergytests.models.dto.cin

import com.google.gson.annotations.SerializedName

data class MCinDto(
    @SerializedName("m2m:cin") val cin: CinDto
)