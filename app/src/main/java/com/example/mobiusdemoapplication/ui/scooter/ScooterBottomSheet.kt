package com.example.mobiusdemoapplication.ui.scooter

sealed class ScooterBottomSheet(val scooterId: String) {

    class GpsLocation(scooterId: String) : ScooterBottomSheet(scooterId)

}
