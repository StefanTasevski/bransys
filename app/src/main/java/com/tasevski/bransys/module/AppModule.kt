package com.tasevski.bransys.module

import android.app.Application
import androidx.room.Room
import com.tasevski.bransys.api.BikeCompanyApi
import com.tasevski.bransys.data.BikeCompanyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BikeCompanyApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideBikeCompanyApi(retrofit: Retrofit): BikeCompanyApi =
        retrofit.create(BikeCompanyApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(app: Application) : BikeCompanyDatabase =
        Room.databaseBuilder(app, BikeCompanyDatabase::class.java, "database")
            .build()
}