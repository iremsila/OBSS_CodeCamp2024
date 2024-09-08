package com.iremsilayildirim.homework1.ui.listpage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.iremsilayildirim.homework1.R
import com.iremsilayildirim.homework1.data.database.DatabaseBuilder
import com.iremsilayildirim.homework1.data.model.FavoriteMovie
import com.iremsilayildirim.homework1.network.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieAdapter(private val onItemClick: (Result) -> Unit) :
    ListAdapter<Result, MovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    enum class LayoutState {
        LIST,
        GRID_2,
        GRID_3
    }

    private var layoutState = LayoutState.LIST

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie, layoutState)

        holder.itemView.findViewById<ImageView>(R.id.moviePoster).translationY = 0f
    }

    fun setLayoutState(state: LayoutState) {
        layoutState = state
        notifyDataSetChanged() // Layout değiştiğinde listeyi güncelle
    }

    inner class MovieViewHolder(itemView: View, private val onItemClick: (Result) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val posterImageView: ImageView = itemView.findViewById(R.id.moviePoster)
        private val titleTextView: TextView = itemView.findViewById(R.id.movieTitle)
        private val heartIcon: ImageView = itemView.findViewById(R.id.heartIcon)

        fun bind(movie: Result, layoutState: LayoutState) {
            titleTextView.text = movie.title

            // LayoutState'e göre poster yüksekliğini ayarla
            val layoutParams = posterImageView.layoutParams
            layoutParams.height = when (layoutState) {
                LayoutState.LIST -> 1800
                LayoutState.GRID_2 -> 700
                LayoutState.GRID_3 -> 700
            }
            posterImageView.layoutParams = layoutParams

            // Glide ile görseli yükleyin
            Glide.with(posterImageView.context)
                .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
                .apply(
                    RequestOptions().override(layoutParams.width, layoutParams.height)
                )
                .into(posterImageView)

            itemView.setOnClickListener {
                onItemClick(movie)
            }

            // Favori simgesini kontrol et ve güncelle
            CoroutineScope(Dispatchers.Main).launch {
                val isFavorite = withContext(Dispatchers.IO) { isFavorite(movie) }
                updateHeartIcon(isFavorite)

                // Favori ikonuna tıklama işlevi
                heartIcon.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        val newIsFavorite = !isFavorite
                        if (newIsFavorite) {
                            addToFavorites(movie)
                        } else {
                            removeFromFavorites(movie)
                        }
                        withContext(Dispatchers.Main) {
                            updateHeartIcon(newIsFavorite)
                        }
                    }
                }
            }
        }

        private suspend fun isFavorite(movie: Result): Boolean {
            // Veritabanında favori olup olmadığını kontrol edin
            val database = DatabaseBuilder.getDatabase(itemView.context)
            return database.favoriteMovieDao().isFavorite(movie.id)
        }

        private fun addToFavorites(movie: Result) {
            CoroutineScope(Dispatchers.IO).launch {
                val database = DatabaseBuilder.getDatabase(itemView.context)
                val favoriteMovie = FavoriteMovie(
                    id = movie.id,
                    title = movie.title ?: "",
                    posterPath = movie.posterPath ?: "",
                    releaseDate = movie.releaseDate ?: "",
                    overview = movie.overview ?: "" // Overview ekliyoruz
                )
                database.favoriteMovieDao().addFavorite(favoriteMovie)
            }
        }

        private fun removeFromFavorites(movie: Result) {
            CoroutineScope(Dispatchers.IO).launch {
                val database = DatabaseBuilder.getDatabase(itemView.context)
                val favoriteMovie = FavoriteMovie(
                    id = movie.id,
                    title = movie.title ?: "",
                    posterPath = movie.posterPath ?: "",
                    releaseDate = movie.releaseDate ?: "",
                    overview = movie.overview ?: "" // Overview ekliyoruz
                )
                database.favoriteMovieDao().removeFavorite(favoriteMovie)
            }
        }

        private fun updateHeartIcon(isFavorite: Boolean) {
            heartIcon.setImageResource(
                if (isFavorite) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline
            )
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
}
