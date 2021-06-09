package com.capstoneproject.silimbah.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataCraft (
        @SerializedName("id")
        @Expose
        val id: Int,

        @SerializedName("title")
        @Expose
        val title: String,

        @SerializedName("image_url")
        @Expose
        val image_url : String,

        @SerializedName("url")
        @Expose
        val url: String
)