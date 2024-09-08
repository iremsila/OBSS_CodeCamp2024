package com.iremsilayildirim.homework1.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iremsilayildirim.homework1.data.model.FavoriteMovie
import com.iremsilayildirim.homework1.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {

    val allFavoriteMovies: LiveData<List<FavoriteMovie>> = repository.getAllFavoriteMovies()

    fun addFavoriteMovie(favoriteMovie: FavoriteMovie) {
        viewModelScope.launch {
            repository.addFavoriteMovie(favoriteMovie)
        }
    }

    fun removeFavoriteMovie(favoriteMovie: FavoriteMovie) {
        viewModelScope.launch {
            repository.removeFavoriteMovie(favoriteMovie)
        }
    }

    suspend fun getFavoriteMovieById(movieId: Int) = repository.getFavoriteMovieById(movieId)
}
