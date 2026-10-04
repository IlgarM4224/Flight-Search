package com.example.flightsearchapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {
    @Query("SELECT * from airport")
    fun getAllAirports(): Flow<List<Airport>>

    @Query("""
            SELECT * from airport
            WHERE iata_code != :departureCode AND name != :departureName
            ORDER BY passengers DESC
        """)
    fun getDestinationAirports(departureCode: String, departureName: String): Flow<List<Airport>>

    @Query("""
        SELECT * from airport
        WHERE iata_code LIKE '%'||:query||'%' 
        OR name LIKE '%'||:query||'%'
        ORDER BY passengers DESC
    """)
    fun getAirportsByQuery(query: String): Flow<List<Airport>>

    @Insert(entity = Favorite::class)
    suspend fun addFavoriteFlight(favorite: Favorite)

    @Query(
        """
        DELETE FROM favorite
        WHERE departure_code = :departureCode AND destination_code = :destinationCode
        """
    )
    suspend fun deleteFavoriteFlight(departureCode: String, destinationCode: String)

    @Query(" SELECT * from favorite ")
    fun getAllFavoriteFlights(): Flow<List<Favorite>>
}