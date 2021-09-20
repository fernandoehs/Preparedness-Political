package com.example.android.politicalpreparedness.representative

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.BR
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.repository.RepresentativeRepository
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.Constants
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RepresentativeViewModel: ViewModel(), Observable {

    //TODO: Establish live data for representatives and address

    //TODO: Create function to fetch representatives from API from a provided address
    private val repository = RepresentativeRepository()
    private val propertyChangeRegistry = PropertyChangeRegistry()


    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    // To set visibility of list placeholder imageView
    private val _status = MutableLiveData<Constants.ApiStatus>()
    val status: LiveData<Constants.ApiStatus>
        get() = _status

    // To set visibility of list placeholder textView
    private val _hasData = MutableLiveData<Boolean>()
    val hasData: LiveData<Boolean>
        get() = _hasData

    init {
        _hasData.value = false
    }



    fun getRepresentativesFromNetwork() {
        viewModelScope.launch {
            _status.value = Constants.ApiStatus.LOADING
            repository.getRepresentative(address.toFormattedString())
                .catch {
                    _status.value = Constants.ApiStatus.ERROR
                    _representatives.value = listOf()
                    _hasData.value = false
                }
                .collect { representativeResponse ->
                    _status.value = Constants.ApiStatus.DONE
                    _hasData.value = true
                    _representatives.value = representativeResponse.offices.flatMap { office ->
                        office.getRepresentatives(representativeResponse.officials)
                    }
                }
        }
    }

    @Bindable
    var address = Address("", "", "", "", "")
        set(value) {
            if (value != field) {
                field = value
                propertyChangeRegistry.notifyChange(
                    this, BR.address
                )
            }
        }

    override  fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        propertyChangeRegistry.add(callback)
    }

    override  fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        propertyChangeRegistry.remove(callback)
    }

}
