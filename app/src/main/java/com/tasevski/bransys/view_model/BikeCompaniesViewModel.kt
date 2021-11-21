package com.tasevski.bransys.view_model


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tasevski.bransys.data.BikeCompanyRepository
import com.tasevski.bransys.data.model.BikeCompany
import com.tasevski.bransys.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BikeCompaniesViewModel @Inject constructor(
    repository: BikeCompanyRepository
) : ViewModel() {
    var bikeCompanies: LiveData<Resource<List<BikeCompany>>>? = null
    private val repositoryNew = repository

    suspend fun deleteAll() {
        repositoryNew.deleteBikeCompanies()
    }

    init {
        viewModelScope.launch {
            bikeCompanies = repository.getBikeCompanies().asLiveData()
        }
    }
}