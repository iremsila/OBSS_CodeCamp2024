package com.iremsilayildirim.homework1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.iremsilayildirim.homework1.network.model.Movie
import com.iremsilayildirim.homework1.network.model.MovieDetail
import com.iremsilayildirim.homework1.repository.MovieDetailRepository
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val repository: MovieDetailRepository) : ViewModel() {

    private val _movieDetail = MutableLiveData<MovieDetail>()
    val movieDetail: LiveData<MovieDetail> = _movieDetail

    private val _similarMovies = MutableLiveData<List<Movie>>()
    val similarMovies: LiveData<List<Movie>> = _similarMovies

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchMovieDetail(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getMovieDetail(movieId)
                _movieDetail.postValue(response)

                // Fetch similar movies by genre
                if (response.genres.isNotEmpty()) {
                    fetchMoviesByGenre(response.genres.first().id)
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    fun fetchMoviesByGenre(genreId: Int) {
        viewModelScope.launch {
            try {
                val movies = repository.getMoviesByGenre(genreId)
                _similarMovies.postValue(movies.shuffled().take(7)) // Shuffle and take 7 movies
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }

    suspend fun addFavoriteMovie(movieDetail: MovieDetail) {
        repository.addFavoriteMovie(movieDetail)
    }

    suspend fun removeFavoriteMovie(movieId: Int) {
        repository.removeFavoriteMovie(movieId)
    }

    suspend fun getFavoriteMovieById(movieId: Int) = repository.getFavoriteMovieById(movieId)
}

class MovieDetailViewModelFactory(private val repository: MovieDetailRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
