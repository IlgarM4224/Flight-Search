package com.example.flightsearchapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearchapp.R
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.ui.theme.FlightSearchAppTheme

@Composable
fun FlightCard(
    modifier: Modifier = Modifier,
    state: FlightRow,
    onFavoriteClick: (FlightRow) -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(topEnd = 24.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.depart),
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                CodeAndName(state.departureAirport.iataCode, state.departureAirport.name)

                Spacer(Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.arrive),
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                CodeAndName(state.destinationAirport.iataCode, state.destinationAirport.name)
            }

            IconButton( onClick = { onFavoriteClick(state) } ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = if (state.isFavorite) Color.Yellow else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun CodeAndName(
    code: String,
    name: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = code,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = name,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}


@Preview
@Composable
fun FlightCardPreview() {
    FlightSearchAppTheme {
        Surface {
            FlightCard(
                state = testState,
                onFavoriteClick = {},
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun FlightCardDarkPreview() {
    FlightSearchAppTheme(darkTheme = true) {
        Surface {
            FlightCard(
                state = testState,
                onFavoriteClick = {},
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }
    }
}

private val testState = FlightRow(
    departureAirport = Airport(
        id = 1,
        iataCode = "FCO",
        name = "Leonardo da Vinci International Airport",
        passengers = 12000,
    ),
    destinationAirport = Airport(
        id = 2,
        iataCode = "SVO",
        name = "Sheremetyevo International Airport",
        passengers = 9000
    ),
    isFavorite = true
)