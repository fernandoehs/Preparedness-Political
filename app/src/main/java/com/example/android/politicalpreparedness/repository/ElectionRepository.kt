package com.example.android.politicalpreparedness.repository

import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ElectionRepository(private val database: ElectionDatabase) {

    // Fetch upcoming Election data from Network
    val getUpcomingElections: Flow<ElectionResponse> = flow {
        val upcomingElections = CivicsApi.retrofitService.getElections()
        emit(upcomingElections)
    }.flowOn(Dispatchers.IO)

    // Fetch Voter Info data from Network
    fun getVoterInfo(address: String, id: Long): Flow<VoterInfoResponse> = flow {
        val voterInfo = CivicsApi.retrofitService.getVoterInfo(address, id)
        emit(voterInfo)
    }.flowOn(Dispatchers.IO)

    // Insert election to room database
    suspend fun insertElection(election: Election){
        database.electionDao.insertElection(election)
    }

    // Fetch all elections from room database
    fun getAllSavedElectionsFromDB(): Flow<List<Election>> = flow {
        val savedElections = database.electionDao.getAllSavedElections()
        emit(savedElections)
    }.flowOn(Dispatchers.IO)

    // Fetch Single election from room database
    fun getSingleElectionFromDB(id: Long): Flow<Election?> = flow {
        val election = database.electionDao.getSingleElection(id)
        emit(election)
    }.flowOn(Dispatchers.IO)

    // Delete election from room database
    suspend fun deleteElection(id: Long) {
        database.electionDao.deleteElection(id)
    }


}