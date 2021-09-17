package com.example.mobiusdemoapplication.ui.gpslocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GpsLocationViewModel : ViewModel() {

    private val _locationState = MutableLiveData<LocationState>(LocationState.Locating)
    val locationState: LiveData<LocationState> = _locationState

    fun locateScooter(id: String) {
        viewModelScope.launch {
            delay(1000)
            if (Random.nextBoolean()) {
                _locationState.value = LocationState.Located(55.7284f, 37.6013f)
            } else {
                _locationState.value = LocationState.Offline
            }
        }
    }

}