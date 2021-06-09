package com.capstoneproject.silimbah.model

import com.google.gson.annotations.SerializedName

data class DataCraft (
        @SerializedName("id")
        val id: Int,

        @SerializedName("title")
        val title: String,

        @SerializedName("image_url")
        val image_url : String,

        @SerializedName("url")
        val url: String
)