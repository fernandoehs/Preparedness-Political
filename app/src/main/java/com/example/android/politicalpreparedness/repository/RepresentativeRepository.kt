package com.example.android.politicalpreparedness.repository


import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RepresentativeRepository {

    // Fetch representative data from Network
    fun getRepresentative(address: String): Flow<RepresentativeResponse> = flow {
        val representative = CivicsApi.retrofitService.getRepresentatives(address)
        emit(representative)
    }.flowOn(Dispatchers.IO)
}