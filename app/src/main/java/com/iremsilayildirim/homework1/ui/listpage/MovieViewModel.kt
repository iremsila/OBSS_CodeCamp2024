package com.iremsilayildirim.homework1.ui.listpage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iremsilayildirim.homework1.network.model.Result
import com.iremsilayildirim.homework1.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {

    private val repository = MovieRepository()

    private val _movieList = MutableLiveData<List<Result>>()
    val movieList: LiveData<List<Result>> get() = _movieList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val currentList = mutableListOf<Result>()

    fun fetchMovies(page: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val movies = repository.getMovies(page).results?.filterNotNull() ?: emptyList()
                currentList.addAll(movies)
                _movieList.value = currentList
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error fetching movies: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
