package com.iremsilayildirim.homework1.ui.detailpage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iremsilayildirim.homework1.databinding.ItemSimilarMovieBinding
import com.iremsilayildirim.homework1.network.model.Movie

class SimilarMoviesAdapter(
    private val movies: List<Movie>,
    private val onItemClick: (Movie) -> Unit // Accept a click listener lambda
) : RecyclerView.Adapter<SimilarMoviesAdapter.SimilarMoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMoviesViewHolder {
        val binding = ItemSimilarMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimilarMoviesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SimilarMoviesViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size

    inner class SimilarMoviesViewHolder(private val binding: ItemSimilarMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.MovieTitle.text = movie.title
            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                .into(binding.moviePoster)

            binding.root.setOnClickListener {
                onItemClick(movie) // Call the click listener with the clicked movie
            }
        }
    }
}
