package com.example.flightsearchapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.FlightRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class FlightSearchAppViewModel(private val flightRepository: FlightRepository): ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _isSearchActive = MutableStateFlow(false)
    private val _selectedAirport = MutableStateFlow<Airport?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _searchResults = _searchQuery.flatMapLatest { query ->
        if (query.isBlank()) flowOf(emptyList())
        else flightRepository.getAirportsByQueryStream(query)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _destinationAirports = _selectedAirport.flatMapLatest { airport ->
        if (airport != null) {
            flightRepository.getDestinationAirportsStream(
                departureCode = airport.iataCode,
                departureName = airport.name
            )
        } else flowOf(emptyList())
    }
    val uiState: StateFlow<FlightSearchUiState> = combine(
        _searchQuery,
        _isSearchActive,
        _searchResults,
        _selectedAirport,
        _destinationAirports
    ) { query, isActive, searchResults, selectedAirport, destinations ->
        FlightSearchUiState(
            searchQuery = query,
            isSearchActive = isActive,
            searchResults = searchResults,
            selectedAirport = selectedAirport,
            destinationAirports = destinations
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FlightSearchUiState()
    )

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
        if (newQuery.isBlank()) {
            _selectedAirport.value = null
        }
    }

    fun onSearchActiveChange(isActive: Boolean) {
        _isSearchActive.value = isActive
    }

    fun selectAirport(airport: Airport) {
        _selectedAirport.value = airport
        _searchQuery.value = airport.iataCode
        _isSearchActive.value = false
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _selectedAirport.value = null
        _isSearchActive.value = false
    }
}

data class FlightSearchUiState(
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val searchResults: List<Airport> = emptyList(),
    val selectedAirport: Airport? = null,
    val destinationAirports: List<Airport> = emptyList()
)

data class FlightRow(
    val departureAirport: Airport,
    val destinationAirport: Airport,
    val isFavorite: Boolean = false
)