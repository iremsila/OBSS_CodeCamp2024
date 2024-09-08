package com.iremsilayildirim.homework1.repository

import com.iremsilayildirim.homework1.network.RetrofitInstance
import com.iremsilayildirim.homework1.network.model.Movies

class MovieRepository {
    suspend fun getMovies(page: Int): Movies {
        return RetrofitInstance.api.getMovies(page)
    }
}
