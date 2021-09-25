package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Division


class VoterInfoViewModelFactory(
    val id: Long,
    val division: Division,
    private val dataSource: ElectionDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            return VoterInfoViewModel(id, division, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}