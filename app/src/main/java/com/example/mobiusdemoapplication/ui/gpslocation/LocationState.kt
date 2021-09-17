package com.example.mobiusdemoapplication.ui.gpslocation

sealed class LocationState {
    object Locating : LocationState()
    object Offline : LocationState()
    class Located(val longitude: Float, val latitude: Float) : LocationState()
}
