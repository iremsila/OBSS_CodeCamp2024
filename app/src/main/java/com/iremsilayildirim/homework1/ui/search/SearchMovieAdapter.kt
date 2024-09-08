package com.iremsilayildirim.homework1.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iremsilayildirim.homework1.R
import com.iremsilayildirim.homework1.data.database.DatabaseBuilder
import com.iremsilayildirim.homework1.network.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchMovieAdapter(
    private var movies: List<Movie>,
    private val onClick: (Movie) -> Unit
) : RecyclerView.Adapter<SearchMovieAdapter.SearchMovieViewHolder>() {

    inner class SearchMovieViewHolder(itemView: View, private val onItemClick: (Movie) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val posterImageView: ImageView = itemView.findViewById(R.id.posterImageView)
        private val movieTitleTextView: TextView = itemView.findViewById(R.id.movieTitleTextView)
        private val heartIcon: ImageView = itemView.findViewById(R.id.heartIcon)

        fun bind(movie: Movie) {
            movieTitleTextView.text = movie.title

            // Poster yükleme
            Glide.with(posterImageView.context)
                .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                .into(posterImageView)

            // Favori durumu kontrol et ve ikonu güncelle
            CoroutineScope(Dispatchers.IO).launch {
                val database = DatabaseBuilder.getDatabase(itemView.context)
                val isFavorite = database.favoriteMovieDao().isFavorite(movie.id)

                withContext(Dispatchers.Main) {
                    updateHeartIcon(isFavorite)
                }
            }

            itemView.setOnClickListener {
                onItemClick(movie)
            }
        }

        private fun updateHeartIcon(isFavorite: Boolean) {
            heartIcon.setImageResource(
                if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_movie, parent, false)
        return SearchMovieViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: SearchMovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}
