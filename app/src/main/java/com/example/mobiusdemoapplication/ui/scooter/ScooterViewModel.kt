package com.example.mobiusdemoapplication.ui.scooter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobiusdemoapplication.data.ScootersRepository
import com.example.mobiusdemoapplication.domain.Scooter
import com.example.mobiusdemoapplication.tea.SingleLiveEvent
import kotlinx.coroutines.launch

class ScooterViewModel(
    private val scootersRepository: ScootersRepository
) : ViewModel() {

    private var userScooters = emptyList<Scooter>()

    private val _userScooter = MutableLiveData<Scooter>()
    val userScooter: LiveData<Scooter> = _userScooter

    private val _connectingStatus = MutableLiveData(BluetoothConnectingStatus.IDLE)
    val connectingStatus: LiveData<BluetoothConnectingStatus> = _connectingStatus

    private val _snackText = SingleLiveEvent<String>()
    val snackText: LiveData<String> = _snackText

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _selectionScooters = MutableLiveData<List<Scooter>?>(null)
    val selectionScooters: LiveData<List<Scooter>?> = _selectionScooters

    private val _scooterBottomSheet = MutableLiveData<ScooterBottomSheet>()
    val scooterBottomSheet: LiveData<ScooterBottomSheet> = _scooterBottomSheet

    init {
        viewModelScope.launch {
            _isLoading.value = true
            userScooters = scootersRepository.loadUserScooters()

            _userScooter.value = userScooters.first()
            _isLoading.value = false
        }
    }

    fun onBluetoothClicked() {
        val scooter = _userScooter.value ?: return
        viewModelScope.launch {
            _connectingStatus.value = BluetoothConnectingStatus.CONNECTING

            if (scootersRepository.connectToScooter(scooter.id)) {
                _connectingStatus.value = BluetoothConnectingStatus.CONNECTED
            } else {
                _connectingStatus.value = BluetoothConnectingStatus.FAILED
                _snackText.setValue("Не удалось подсоединиться к самокату")
            }
        }
    }

    fun onLocateClicked() {
        val scooterId = _userScooter.value?.id ?: return
        _scooterBottomSheet.value = ScooterBottomSheet.GpsLocation(scooterId)
    }

    fun onSelectScooterClicked() {
        _selectionScooters.value = _selectionScooters.value.let { scooters ->
            if (scooters == null) {
                userScooters
            } else {
                null
            }
        }
    }

    fun onScooterSelected(scooterId: String) {
        _selectionScooters.value = null
        _connectingStatus.value = BluetoothConnectingStatus.IDLE
        _userScooter.value = userScooters.first { it.id == scooterId }
    }
}