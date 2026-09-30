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
import kotlinx.coroutines.flow.update

private data class SearchParams(
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val selectedAirport: Airport? = null
)

class FlightSearchAppViewModel(private val flightRepository: FlightRepository): ViewModel() {
    private val _searchParams = MutableStateFlow(SearchParams())
    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<FlightSearchUiState> = _searchParams
        .flatMapLatest { params ->
            val searchResultsFlow = if (params.searchQuery.isBlank()) flowOf(emptyList())
                else  flightRepository.getAirportsByQueryStream(params.searchQuery)

            val destinationsFlow = if (params.selectedAirport != null) {
                flightRepository.getDestinationAirportsStream(
                    departureCode = params.selectedAirport.iataCode,
                    departureName = params.selectedAirport.name
                )
            } else flowOf(emptyList())


            combine(searchResultsFlow, destinationsFlow) { searchResults, destinations ->
                FlightSearchUiState(
                    searchQuery = params.searchQuery,
                    isSearchActive = params.isSearchActive,
                    searchResults = searchResults,
                    selectedAirport = params.selectedAirport,
                    destinationAirports = destinations
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FlightSearchUiState()
        )

    fun onQueryChange(newQuery: String) {
        _searchParams.update { current ->
            current.copy(
                searchQuery = newQuery,
                selectedAirport = if (newQuery.isBlank()) null else current.selectedAirport
            )
        }
    }

    fun onSearchActiveChange(isActive: Boolean) {
        _searchParams.update { it.copy(isSearchActive = isActive) }
    }

    fun selectAirport(airport: Airport) {
        _searchParams.update { current ->
            current.copy(
                selectedAirport = airport,
                searchQuery = airport.iataCode,
                isSearchActive = false
            )
        }
    }

    fun clearSearch() {
        _searchParams.update { SearchParams() }
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