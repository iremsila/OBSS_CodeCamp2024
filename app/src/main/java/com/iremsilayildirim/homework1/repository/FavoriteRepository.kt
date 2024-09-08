package com.iremsilayildirim.homework1.repository

import androidx.lifecycle.LiveData
import com.iremsilayildirim.homework1.data.database.AppDatabase
import com.iremsilayildirim.homework1.data.model.FavoriteMovie

class FavoriteRepository(private val database: AppDatabase) {

    fun getAllFavoriteMovies(): LiveData<List<FavoriteMovie>> {
        return database.favoriteMovieDao().getAllFavorites() // LiveData olarak döndür
    }

    suspend fun addFavoriteMovie(favoriteMovie: FavoriteMovie) {
        database.favoriteMovieDao().addFavorite(favoriteMovie)
    }

    suspend fun removeFavoriteMovie(favoriteMovie: FavoriteMovie) {
        database.favoriteMovieDao().removeFavorite(favoriteMovie)
    }

    suspend fun getFavoriteMovieById(movieId: Int): FavoriteMovie? {
        return database.favoriteMovieDao().getFavoriteById(movieId)
    }
}
