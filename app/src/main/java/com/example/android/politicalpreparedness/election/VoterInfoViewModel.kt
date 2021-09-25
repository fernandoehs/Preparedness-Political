package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.State
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.utils.Constants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    val id: Long,
    val division: Division,
    dataSource: ElectionDatabase
) : ViewModel() {
    private val repository = ElectionRepository(dataSource)
    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String>
        get() = _errorMsg

    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private val _stateInfo = MutableLiveData<State>()
    val stateInfo: LiveData<State>
        get() = _stateInfo

    private val _status = MutableLiveData<Constants.ApiStatus>()
    val status: LiveData<Constants.ApiStatus>
        get() = _status

    init {
        getVoterInfoFromNetwork()
        getSingleElectionFromDB()
    }

    private fun getVoterInfoFromNetwork() {
        viewModelScope.launch {
            _status.value = Constants.ApiStatus.LOADING
            repository.getVoterInfo(division.state, id)
                .catch {
                    _status.value = Constants.ApiStatus.ERROR
                    _errorMsg.value = it.message
                }
                .collect { voterInfo ->
                    _status.value = Constants.ApiStatus.DONE
                    _voterInfo.value = voterInfo
                    _stateInfo.value = voterInfo.state?.get(0)
                }
        }
    }

    private val _navigateToVotingUrl = MutableLiveData<String>()
    val navigateToVotingUrl: LiveData<String>
        get() = _navigateToVotingUrl

    fun openVotingLocationUrl() {
        _navigateToVotingUrl.value = stateInfo.value?.electionAdministrationBody?.votingLocationFinderUrl
    }

    fun openVotingLocationComplete() {
        _navigateToVotingUrl.value = null
    }

    private val _navigateToBallotUrl = MutableLiveData<String>()
    val navigateToBallotUrl: LiveData<String>
        get() = _navigateToBallotUrl

    fun openBallotUrl() {
        _navigateToBallotUrl.value = stateInfo.value?.electionAdministrationBody?.ballotInfoUrl
    }

    fun openBallotUrlComplete() {
        _navigateToBallotUrl.value = null
    }

    private val _isElectionSaved = MutableLiveData<Boolean>()
    val isElectionSaved: LiveData<Boolean>
        get() = _isElectionSaved

    private fun getSingleElectionFromDB() {
        viewModelScope.launch {
            repository.getSingleElectionFromDB(id)
                .catch {
                    _errorMsg.value = it.message
                }
                .collect { election ->
                    _isElectionSaved.value = election != null
                }
        }
    }

    fun toggleButton() {
        if (_isElectionSaved.value == false) {
            saveElection()
        } else {
            deleteSingleElection()
        }
    }

    private fun saveElection() {
        val election = voterInfo.value!!.election
        viewModelScope.launch {
            repository.insertElection(election)
            _isElectionSaved.value = true
        }
    }

    private fun deleteSingleElection() {
        viewModelScope.launch {
            repository.deleteElection(id)
            _isElectionSaved.value = false
        }
    }

}