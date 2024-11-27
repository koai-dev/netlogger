package com.koai.sample.service

import com.koai.sample.model.LocationModel
import retrofit2.http.GET

interface ApiService {
    @GET("json/")
    suspend fun getLocation(): LocationModel
}