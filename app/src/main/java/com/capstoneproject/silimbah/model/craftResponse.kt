package com.capstoneproject.silimbah.model

import com.google.gson.annotations.SerializedName

data class craftResponse (
    @field:SerializedName("data")
    val data: List<DataCraft>
        )