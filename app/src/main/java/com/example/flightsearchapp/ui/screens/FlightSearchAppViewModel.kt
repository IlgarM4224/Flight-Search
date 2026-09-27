package com.example.flightsearchapp.ui.screens

import androidx.lifecycle.ViewModel
import com.example.flightsearchapp.data.Airport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SearchResult(
    val list: List<String>
)
class FlightSearchAppViewModel: ViewModel() {
    private var _uiState = MutableStateFlow("")

    val uiState = _uiState.asStateFlow()
}

data class FlightRow(
    val departureAirport: Airport,
    val destinationAirport: Airport,
    val isFavorite: Boolean = false
)