package com.example.flightsearchapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flightsearchapp.ui.theme.FlightSearchAppTheme


@Composable
fun FlightSearchApp(
    modifier: Modifier,
    viewModel: FlightSearchAppViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    FlightSearchAppContent(
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchAppContent(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Flight Search",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                modifier = modifier,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

        }
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