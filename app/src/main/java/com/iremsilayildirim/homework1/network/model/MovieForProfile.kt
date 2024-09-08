package com.iremsilayildirim.homework1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_for_profile")
data class MovieForProfile(
    @PrimaryKey val id: Int,
    val title: String,
    val lastVisited: Long,
    val posterUrl: String
)
