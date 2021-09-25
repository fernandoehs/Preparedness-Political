package com.example.android.politicalpreparedness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert
    suspend fun insertElection(election: Election)

    // select all election query
    @Query("SELECT * FROM ELECTION_TABLE ORDER BY id ASC")
    fun getAllSavedElections(): List<Election>

    // select single election query
    @Query("SELECT * FROM ELECTION_TABLE WHERE :electionId = id ")
    fun getSingleElection(electionId: Long): Election

    // delete query
    @Query("DELETE FROM ELECTION_TABLE WHERE :electionId = id")
    suspend fun deleteElection(electionId: Long)

    // clear query
    @Query("DELETE FROM ELECTION_TABLE")
    suspend fun clearAll()

}