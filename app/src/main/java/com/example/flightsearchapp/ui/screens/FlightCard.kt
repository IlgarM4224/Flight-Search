package com.example.flightsearchapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearchapp.R
import com.example.flightsearchapp.data.Airport
import com.example.flightsearchapp.ui.theme.FlightSearchAppTheme

private val FavoriteStarColor = Color(0xFFFFB300)

@Composable
fun FlightCard(
    state: FlightRow,
    onFavoriteClick: (FlightRow) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 16.dp, bottom = 16.dp, end = 8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AirportInfo(
                    label = stringResource(R.string.depart),
                    airport = state.departureAirport
                )
                AirportInfo(
                    label = stringResource(R.string.arrive),
                    airport = state.destinationAirport,
                    highlighted = true
                )
            }

            FavoriteButton(
                isFavorite = state.isFavorite,
                onClick = { onFavoriteClick(state) }
            )
        }
    }
}

@Composable
private fun AirportInfo(
    label: String,
    airport: Airport,
    modifier: Modifier = Modifier,
    highlighted: Boolean = false,
) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.outline,
            fontWeight = FontWeight.Bold,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = airport.iataCode,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (highlighted) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outlineVariant,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(16.dp)
            )
            Text(
                text = airport.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
        }
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tint by animateColorAsState(
        targetValue = if (isFavorite) FavoriteStarColor else MaterialTheme.colorScheme.outline,
        label = "favoriteTint"
    )

    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
            contentDescription = stringResource(
                if (isFavorite) R.string.remove_favorite else R.string.add_favorite
            ),
            tint = tint,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Preview
@Composable
private fun FlightCardPreview() {
    FlightSearchAppTheme {
        Surface {
            FlightCard(
                state = testState,
                onFavoriteClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun FlightCardDarkPreview() {
    FlightSearchAppTheme(darkTheme = true) {
        Surface {
            FlightCard(
                state = testState,
                onFavoriteClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
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