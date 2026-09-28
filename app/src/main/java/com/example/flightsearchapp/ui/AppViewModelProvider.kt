package com.example.flightsearchapp.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearchapp.FlightSearchApplication
import com.example.flightsearchapp.ui.screens.FlightSearchAppViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            FlightSearchAppViewModel(flightSearchApplication().container.flightRepository)
        }
    }
}

fun CreationExtras.flightSearchApplication(): FlightSearchApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as FlightSearchApplication)