package com.example.flightsearchapp.data

import kotlinx.coroutines.flow.Flow

class OfflineFlightRepository(private val flightDao: FlightDao): FlightRepository {
    override fun getAllAirportsStream(): Flow<List<Airport>> = flightDao.getAllAirports()

    override fun getDestinationAirportsStream(
        departureCode: String,
        departureName: String
    ): Flow<List<Airport>> = flightDao.getDestinationAirports(departureCode, departureName)

    override fun getAirportsByQueryStream(query: String): Flow<List<Airport>> = flightDao.getAirportsByQuery(query)

    override suspend fun addFavoriteFlight(favorite: Favorite) = flightDao.addFavoriteFlight(favorite)

    override suspend fun deleteFavoriteFlight(favorite: Favorite) = flightDao.deleteFavoriteFlight(favorite)

    override fun getAllFavoriteFlightsStream(): Flow<List<Favorite>> = flightDao.getAllFavoriteFlights()
}