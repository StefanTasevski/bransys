package com.tasevski.bransys.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bikecompanies")
data class BikeCompany(
    @PrimaryKey val id: String,
    val name: String,
    @Embedded val location: CompanyLocation,
)