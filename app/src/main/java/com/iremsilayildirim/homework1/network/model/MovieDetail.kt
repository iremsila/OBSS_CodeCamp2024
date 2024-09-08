package com.iremsilayildirim.homework1.network.model

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val release_date: String,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("vote_average") val voteAverage:Double?
)

data class Genre(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
