package com.tasevski.bransys.api

import com.tasevski.bransys.data.model.Networks
import retrofit2.http.GET

interface BikeCompanyApi {

    companion object {
        const val BASE_URL = "http://api.citybik.es/v2/"
    }

    @GET("networks")
    suspend fun getBikeCompanies(): Networks
}