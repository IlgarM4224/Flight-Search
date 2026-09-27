package com.example.flightsearchapp.data

import kotlinx.coroutines.flow.Flow

interface FlightRepository {
    fun getAllAirportsStream(): Flow<List<Airport>>

    fun getDestinationAirportsStream(departureCode: String, departureName: String): Flow<List<Airport>>

    fun getAirportsByQueryStream(query: String): Flow<List<Airport>>
}