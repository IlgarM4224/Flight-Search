package com.example.flightsearchapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.data.Favorite
import com.example.flightsearchapp.data.FlightRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private data class SearchParams(
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val selectedAirport: Airport? = null
)

class FlightSearchAppViewModel(private val flightRepository: FlightRepository) : ViewModel() {

    private val _searchParams = MutableStateFlow(SearchParams())

    /** All favorite routes along with airport data. */
    private val favoriteFlightsFlow: Flow<List<FlightRow>> = combine(
        flightRepository.getAllFavoriteFlightsStream(),
        flightRepository.getAllAirportsStream()
    ) { favorites, airports ->
        val airportsByCode = airports.associateBy { it.iataCode }

        favorites.mapNotNull { favorite ->
            val departure = airportsByCode[favorite.departureCode]
            val destination = airportsByCode[favorite.destinationCode]
            if (departure != null && destination != null) {
                FlightRow(departure, destination, isFavorite = true)
            } else null
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<FlightSearchUiState> = _searchParams
        .flatMapLatest { params ->
            val searchResultsFlow =
                if (params.searchQuery.isBlank()) flowOf(emptyList())
                else flightRepository.getAirportsByQueryStream(params.searchQuery)

            val destinationsFlow = params.selectedAirport?.let { departure ->
                combine(
                    flightRepository.getDestinationAirportsStream(
                        departureCode = departure.iataCode,
                        departureName = departure.name
                    ),
                    flightRepository.getAllFavoriteFlightsStream()
                ) { destinations, favorites ->
                    val favoriteCodes = favorites
                        .filter { it.departureCode == departure.iataCode }
                        .mapTo(HashSet()) { it.destinationCode }

                    destinations.map { destination ->
                        FlightRow(
                            departureAirport = departure,
                            destinationAirport = destination,
                            isFavorite = destination.iataCode in favoriteCodes
                        )
                    }
                }
            } ?: flowOf(emptyList())

            // The Favorites section is needed only on the main screen (when no airport has been selected or requested)
            val favoritesFlow =
                if (params.searchQuery.isBlank() && params.selectedAirport == null) favoriteFlightsFlow
                else flowOf(emptyList())

            combine(searchResultsFlow, destinationsFlow, favoritesFlow) { searchResults, destinations, favorites ->
                FlightSearchUiState(
                    searchQuery = params.searchQuery,
                    isSearchActive = params.isSearchActive,
                    searchResults = searchResults,
                    selectedAirport = params.selectedAirport,
                    destinationAirports = destinations,
                    favoriteFlights = favorites
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
        _searchParams.update {
            it.copy(selectedAirport = airport, searchQuery = airport.iataCode, isSearchActive = false)
        }
    }

    fun clearSearch() {
        _searchParams.update { SearchParams() }
    }

    fun toggleFavorite(flightRow: FlightRow) {
        val departureCode = flightRow.departureAirport.iataCode
        val destinationCode = flightRow.destinationAirport.iataCode

        viewModelScope.launch {
            if (flightRow.isFavorite) {
                flightRepository.deleteFavoriteFlight(departureCode, destinationCode)
            } else {
                flightRepository.addFavoriteFlight(Favorite(departureCode = departureCode, destinationCode = destinationCode))
            }
        }
    }
}

data class FlightSearchUiState(
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val searchResults: List<Airport> = emptyList(),
    val selectedAirport: Airport? = null,
    val destinationAirports: List<FlightRow> = emptyList(),
    val favoriteFlights: List<FlightRow> = emptyList()
)

data class FlightRow(
    val departureAirport: Airport,
    val destinationAirport: Airport,
    val isFavorite: Boolean = false
) {
    /** A unique row key for LazyColumn */
    val key: String get() = "${departureAirport.id}-${destinationAirport.id}"
}