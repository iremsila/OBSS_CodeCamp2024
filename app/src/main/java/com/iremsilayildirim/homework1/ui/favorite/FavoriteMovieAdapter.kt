package com.iremsilayildirim.homework1.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iremsilayildirim.homework1.R
import com.iremsilayildirim.homework1.data.model.FavoriteMovie
import com.iremsilayildirim.homework1.databinding.ItemFavoriteMovieBinding

class FavoriteMovieAdapter(
    private val onItemClick: (FavoriteMovie) -> Unit
) : ListAdapter<FavoriteMovie, FavoriteMovieAdapter.FavoriteMovieViewHolder>(FavoriteMovieDiffCallback()) {

    inner class FavoriteMovieViewHolder(private val binding: ItemFavoriteMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: FavoriteMovie) {
            val baseUrl = "https://image.tmdb.org/t/p/w500"
            val fullImageUrl = "$baseUrl${movie.posterPath}"

            Glide.with(binding.root.context)
                .load(fullImageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(binding.imageViewPoster)

            binding.textViewTitle.text = movie.title
            binding.textViewOverview.text = movie.overview

            binding.root.setOnClickListener {
                onItemClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMovieViewHolder {
        val binding = ItemFavoriteMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class FavoriteMovieDiffCallback : DiffUtil.ItemCallback<FavoriteMovie>() {
    override fun areItemsTheSame(oldItem: FavoriteMovie, newItem: FavoriteMovie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FavoriteMovie, newItem: FavoriteMovie): Boolean {
        return oldItem == newItem
    }
}
