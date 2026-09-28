package com.example.flightsearchapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.ui.AppViewModelProvider
import com.example.flightsearchapp.ui.theme.FlightSearchAppTheme


@Composable
fun FlightSearchApp(
    modifier: Modifier,
    viewModel: FlightSearchAppViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    FlightSearchAppContent(
        modifier = modifier,
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onActiveChange = viewModel::onSearchActiveChange,
        onAirportSelect = viewModel::selectAirport,
        onClearClick = viewModel::clearSearch,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchAppContent(
    modifier: Modifier = Modifier,
    state: FlightSearchUiState = FlightSearchUiState(),
    onQueryChange: (String) -> Unit = {},
    onActiveChange: (Boolean) -> Unit = {},
    onAirportSelect: (Airport) -> Unit = {},
    onClearClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Flight Search",
                        style = MaterialTheme.typography.headlineLarge,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = state.searchQuery,
                        onQueryChange = onQueryChange,
                        onSearch = { onActiveChange(false) },
                        expanded = state.isSearchActive,
                        onExpandedChange = onActiveChange,
                        placeholder = { Text("Enter airport name or IATA code") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        trailingIcon = {
                            if (state.searchQuery.isNotEmpty()) {
                                IconButton(onClick = onClearClick) {
                                    Icon(Icons.Default.Clear, "Clear search")
                                }
                            }
                        },
                    )
                },
                expanded = state.isSearchActive,
                onExpandedChange = onActiveChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (state.isSearchActive) 0.dp else 16.dp),
            ) {
                LazyColumn(Modifier.fillMaxWidth()) {
                    items(state.searchResults) {
                        AirportSearchSuggestionItem(
                            airport = it,
                            onClick = { onAirportSelect(it) }
                        )
                    }
                }
            }

            if (!state.isSearchActive) {
                if (state.selectedAirport != null) {
                    Text(
                        text = "Flights from ${state.selectedAirport.iataCode}",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.destinationAirports) { destination ->
                            val stateForCard = FlightRow(
                                departureAirport = state.selectedAirport,
                                destinationAirport = destination
                            )

                            FlightCard(
                                state = stateForCard,
                                onFavoriteClick = {},
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Search for an airport to see flights")
                    }
                }
            }
        }
    }
}

@Composable
fun AirportSearchSuggestionItem(
    airport: Airport,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = airport.iataCode, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = airport.name)
    }
}

@Preview(showSystemUi = true)
@Composable
fun FlightSearchAppPreview() {
    FlightSearchAppTheme {
        Surface {
            FlightSearchAppContent(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun FlightSearchAppDarkPreview() {
    FlightSearchAppTheme(darkTheme = true) {
        Surface {
            FlightSearchAppContent(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}