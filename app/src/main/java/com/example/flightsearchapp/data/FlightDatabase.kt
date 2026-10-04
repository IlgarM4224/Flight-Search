package com.example.flightsearchapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Airport::class, Favorite::class], version = 1, exportSchema = true)
abstract class FlightDatabase: RoomDatabase() {
    abstract fun flightDao(): FlightDao
    companion object {
        @Volatile
        private var Instance: FlightDatabase? = null
        fun getDatabase(context: Context): FlightDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FlightDatabase::class.java, "flight_search_app.db")
                    .createFromAsset("database/flight_search.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}