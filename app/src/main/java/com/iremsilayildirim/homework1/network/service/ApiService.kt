package com.iremsilayildirim.homework1.network.service

import com.iremsilayildirim.homework1.network.model.MovieDetail
import com.iremsilayildirim.homework1.network.model.MovieListResponse
import com.iremsilayildirim.homework1.network.model.MovieResponse
import com.iremsilayildirim.homework1.network.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkYzhmNjE2NzE4ZDY0ZjY2NTkyYTc5NzQzNTMxMTdlYyIsIm5iZiI6MTcyMjQ2MjkxNi42NjIzMTMsInN1YiI6IjY2YTc4ZTE3NWI3ZTZlMWQ2ODExZmQ0NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BGMAW4UrNYpBisDHyVxHgzs1YnTAMv2zXWAZ4zhnSks")
    suspend fun getMovies(@Query("page") page: Int): Movies

    @GET("movie/{movie_id}")
    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkYzhmNjE2NzE4ZDY0ZjY2NTkyYTc5NzQzNTMxMTdlYyIsIm5iZiI6MTcyMjQ2MjkxNi42NjIzMTMsInN1YiI6IjY2YTc4ZTE3NWI3ZTZlMWQ2ODExZmQ0NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BGMAW4UrNYpBisDHyVxHgzs1YnTAMv2zXWAZ4zhnSks")
    suspend fun getMovieDetail(@Path("movie_id") movieId: Int, @Query("language") language: String = "en-US"): MovieDetail

    @GET("discover/movie")
    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkYzhmNjE2NzE4ZDY0ZjY2NTkyYTc5NzQzNTMxMTdlYyIsIm5iZiI6MTcyMjQ2MjkxNi42NjIzMTMsInN1YiI6IjY2YTc4ZTE3NWI3ZTZlMWQ2ODExZmQ0NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BGMAW4UrNYpBisDHyVxHgzs1YnTAMv2zXWAZ4zhnSks")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: Int,
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): MovieListResponse

    @GET("search/movie")
    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkYzhmNjE2NzE4ZDY0ZjY2NTkyYTc5NzQzNTMxMTdlYyIsIm5iZiI6MTcyMjQ2MjkxNi42NjIzMTMsInN1YiI6IjY2YTc4ZTE3NWI3ZTZlMWQ2ODExZmQ0NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BGMAW4UrNYpBisDHyVxHgzs1YnTAMv2zXWAZ4zhnSks")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("discover/movie")
    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkYzhmNjE2NzE4ZDY0ZjY2NTkyYTc5NzQzNTMxMTdlYyIsIm5iZiI6MTcyMjQ2MjkxNi42NjIzMTMsInN1YiI6IjY2YTc4ZTE3NWI3ZTZlMWQ2ODExZmQ0NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BGMAW4UrNYpBisDHyVxHgzs1YnTAMv2zXWAZ4zhnSks")
    suspend fun getMoviesByCategory(
        @Query("with_genres") genreId: Int
    ): Response<MovieResponse>

    @GET("find/{imdb_id}")
    @Headers("Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkYzhmNjE2NzE4ZDY0ZjY2NTkyYTc5NzQzNTMxMTdlYyIsIm5iZiI6MTcyMjQ2MjkxNi42NjIzMTMsInN1YiI6IjY2YTc4ZTE3NWI3ZTZlMWQ2ODExZmQ0NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.BGMAW4UrNYpBisDHyVxHgzs1YnTAMv2zXWAZ4zhnSks")
    suspend fun getMovieByImdbId(
        @Path("imdb_id") imdbId: String,
        @Query("external_source") externalSource: String = "imdb_id"
    ): Response<MovieResponse>
}
