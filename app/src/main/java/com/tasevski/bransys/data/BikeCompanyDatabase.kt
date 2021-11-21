package com.tasevski.bransys.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tasevski.bransys.data.model.BikeCompany

@Database(entities = [BikeCompany::class], version = 1)
abstract class BikeCompanyDatabase : RoomDatabase() {

    abstract fun bikeCompaniesDao(): BikeCompanyDao
}