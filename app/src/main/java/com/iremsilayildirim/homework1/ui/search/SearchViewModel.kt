package com.iremsilayildirim.homework1.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iremsilayildirim.homework1.network.model.Movie

class SearchViewModel : ViewModel() {

    private val _horrorMovie = MutableLiveData<Movie?>()
    val horrorMovie: LiveData<Movie?> get() = _horrorMovie

    private val _animationMovie = MutableLiveData<Movie?>()
    val animationMovie: LiveData<Movie?> get() = _animationMovie

    private val _adventureMovie = MutableLiveData<Movie?>()
    val adventureMovie: LiveData<Movie?> get() = _adventureMovie

    private val _comedyMovie = MutableLiveData<Movie?>()
    val comedyMovie: LiveData<Movie?> get() = _comedyMovie

    fun setHorrorMovie(movie: Movie) {
        _horrorMovie.value = movie
    }

    fun setAnimationMovie(movie: Movie) {
        _animationMovie.value = movie
    }

    fun setAdventureMovie(movie: Movie) {
        _adventureMovie.value = movie
    }

    fun setComedyMovie(movie: Movie) {
        _comedyMovie.value = movie
    }

    fun areMoviesLoaded(): Boolean {
        return horrorMovie.value != null &&
            animationMovie.value != null &&
            adventureMovie.value != null &&
            comedyMovie.value != null
    }
}
