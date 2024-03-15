package com.example.composing.ui.home

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface ApodEndpoint {


    @GET("planetary/apod")
    suspend fun getApod(
        @Query("api_key") apiKey: String,
        @Query("count") count: Int? = 5
    ) : List<Apod>
}