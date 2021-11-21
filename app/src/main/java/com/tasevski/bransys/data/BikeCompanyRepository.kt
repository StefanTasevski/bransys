package com.tasevski.bransys.data

import androidx.room.withTransaction
import com.tasevski.bransys.api.BikeCompanyApi
import com.tasevski.bransys.util.networkBoundResource
import javax.inject.Inject

class BikeCompanyRepository @Inject constructor(
    private val api: BikeCompanyApi,
    private val db: BikeCompanyDatabase
) {
    private val bikeCompaniesDao = db.bikeCompaniesDao()

    suspend fun deleteBikeCompanies() {
        bikeCompaniesDao.deleteAllBikeCompanies()
    }

    suspend fun getBikeCompanies() = networkBoundResource(
        query = {
            bikeCompaniesDao.getAllBikeCompanies()
        },
        fetch = {
            api.getBikeCompanies().networks
        },
        saveFetchResult = { bikeCompanies ->
            db.withTransaction {
                bikeCompaniesDao.deleteAllBikeCompanies()
                bikeCompaniesDao.insertBikeCompanies(bikeCompanies)
            }
        }
    )
}