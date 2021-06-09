package com.capstoneproject.silimbah.model

import com.google.gson.annotations.SerializedName

data class DataCraft (
        @SerializedName("title")
        val title: String,

        @SerializedName("image_url")
        val image_url : String,

        @SerializedName("url")
        val url: String
)