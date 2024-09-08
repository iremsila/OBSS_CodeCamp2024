package com.iremsilayildirim.homework1.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iremsilayildirim.homework1.data.dao.FavoriteMovieDao
import com.iremsilayildirim.homework1.data.dao.ProfileDao
import com.iremsilayildirim.homework1.data.dao.UserDao
import com.iremsilayildirim.homework1.data.model.FavoriteMovie
import com.iremsilayildirim.homework1.data.model.MovieForProfile
import com.iremsilayildirim.homework1.data.model.User

@Database(entities = [User::class, FavoriteMovie::class, MovieForProfile::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun profileDao(): ProfileDao
}
