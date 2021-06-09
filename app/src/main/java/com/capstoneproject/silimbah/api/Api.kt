package com.capstoneproject.silimbah.api

import com.capstoneproject.silimbah.model.craftResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("api/limbah")

    fun getSearchUsers(
        @Query("jenis") query: String
    ) : Call<craftResponse>
}