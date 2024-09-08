package com.iremsilayildirim.homework1.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.iremsilayildirim.homework1.data.model.MovieForProfile

@Dao
interface ProfileDao {

    @Query("SELECT * FROM movies_for_profile ORDER BY lastVisited DESC LIMIT 3")
    fun getRecentlyVisitedMovies(): LiveData<List<MovieForProfile>>
}
