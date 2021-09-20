package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionRepository
import com.example.android.politicalpreparedness.utils.Constants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(val database: ElectionDatabase) : ViewModel() {

    private val repository = ElectionRepository(database)

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>?>()
    val savedElections: LiveData<List<Election>?>
        get() = _savedElections

    // To set visibility of list placeholder imageView
    private val _status = MutableLiveData<Constants.ApiStatus>()
    val status: LiveData<Constants.ApiStatus>
        get() = _status

    // To set visibility of list placeholder textView
    private val _hasData = MutableLiveData<Boolean>()
    val hasData: LiveData<Boolean>
        get() = _hasData

    init {
        getUpcomingElectionsFromNetwork()
        _hasData.value = false
    }

    //: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    private fun getUpcomingElectionsFromNetwork() {
        viewModelScope.launch {
            _status.value = Constants.ApiStatus.LOADING
            repository.getUpcomingElections
                .catch {
                    _status.value = Constants.ApiStatus.ERROR
                }
                .collect { elections ->
                    _status.value = Constants.ApiStatus.DONE
                    _upcomingElections.value = elections.elections
                }
        }
    }

    fun getAllSavedElectionsFromDB() {
        viewModelScope.launch {
            repository.getAllSavedElectionsFromDB()
                .catch {
                    _hasData.value = false
                }
                .collect { savedElections ->
                    _savedElections.value = savedElections
                    if (savedElections.isNotEmpty()) {
                        _hasData.value = true
                    }
                }
        }
    }

    //: Create functions to navigate to saved or upcoming election voter info
    private val _navigateToVoterInfoFragment = MutableLiveData<Election?>()
    val navigateToVoterInfoFragment: MutableLiveData<Election?>
        get() = _navigateToVoterInfoFragment

    fun onElectionClicked(election: Election) {
        _navigateToVoterInfoFragment.value = election
    }

    fun onNavigationComplete() {
        _navigateToVoterInfoFragment.value = null
    }


}