package com.capstoneproject.silimbah.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class craftResponse (
    @field:SerializedName("data")
    @Expose
    val data: List<DataCraft>
        )