package com.iremsilayildirim.homework1.network.service

import com.iremsilayildirim.homework1.network.model.MovieDetail
import com.iremsilayildirim.homework1.network.model.MovieListResponse
import com.iremsilayildirim.homework1.network.model.MovieResponse
import com.iremsilayildirim.homework1.network.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    companion object {
        private const val API_KEY = 

    @GET("movie/popular")
    suspend fun getMovies(
        @Header("Authorization") authorization: String = "Bearer $API_KEY",
        @Query("page") page: Int
    ): Movies

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Header("Authorization") authorization: String = "Bearer $API_KEY",
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): MovieDetail

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Header("Authorization") authorization: String = "Bearer $API_KEY",
        @Query("with_genres") genreId: Int,
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): MovieListResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Header("Authorization") authorization: String = "Bearer $API_KEY",
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("discover/movie")
    suspend fun getMoviesByCategory(
        @Header("Authorization") authorization: String = "Bearer $API_KEY",
        @Query("with_genres") genreId: Int
    ): Response<MovieResponse>
}
