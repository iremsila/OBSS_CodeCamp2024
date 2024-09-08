package com.iremsilayildirim.homework1.repository

import com.iremsilayildirim.homework1.data.database.AppDatabase
import com.iremsilayildirim.homework1.data.model.FavoriteMovie
import com.iremsilayildirim.homework1.network.RetrofitInstance
import com.iremsilayildirim.homework1.network.model.Movie
import com.iremsilayildirim.homework1.network.model.MovieDetail

class MovieDetailRepository(private val database: AppDatabase) {

    // API'den film detaylarını alır
    suspend fun getMovieDetail(movieId: Int): MovieDetail {
        val apiKey = "dc8f616718d64f66592a7974353117ec" // API anahtarınızı buraya ekleyin
        return RetrofitInstance.api.getMovieDetail(movieId, apiKey)
    }

    // API'den belirli bir türdeki filmleri alır
    suspend fun getMoviesByGenre(genreId: Int): List<Movie> {
        val apiKey = "dc8f616718d64f66592a7974353117ec" // API anahtarınızı buraya ekleyin
        return RetrofitInstance.api.getMoviesByGenre(apiKey, genreId).results
    }

    // Veritabanına favori film ekler
    suspend fun addFavoriteMovie(movieDetail: MovieDetail) {
        val favoriteMovie = FavoriteMovie(
            id = movieDetail.id,
            title = movieDetail.title,
            posterPath = movieDetail.posterPath ?: "",
            releaseDate = movieDetail.release_date ?: "",
            overview = movieDetail.overview ?: ""
        )
        database.favoriteMovieDao().addFavorite(favoriteMovie)
    }

    // Veritabanından favori film çıkarır
    suspend fun removeFavoriteMovie(movieId: Int) {
        val favoriteMovie = database.favoriteMovieDao().getFavoriteById(movieId)
        if (favoriteMovie != null) {
            database.favoriteMovieDao().removeFavorite(favoriteMovie)
        }
    }

    // Veritabanından favori film kontrolü
    suspend fun getFavoriteMovieById(movieId: Int) = database.favoriteMovieDao().getFavoriteById(movieId)
}
