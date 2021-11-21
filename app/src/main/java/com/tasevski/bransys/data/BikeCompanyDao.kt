package com.tasevski.bransys.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tasevski.bransys.data.model.BikeCompany
import kotlinx.coroutines.flow.Flow

@Dao
interface BikeCompanyDao {

    @Query("SELECT * FROM bikecompanies")
    fun getAllBikeCompanies(): Flow<List<BikeCompany>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBikeCompanies(bikeCompanies: List<BikeCompany>)

    @Query("DELETE FROM bikecompanies")
    suspend fun deleteAllBikeCompanies()
}