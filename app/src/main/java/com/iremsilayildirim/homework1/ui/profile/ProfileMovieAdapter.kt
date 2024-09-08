package com.iremsilayildirim.homework1.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iremsilayildirim.homework1.databinding.ItemProfileMovieBinding
import com.iremsilayildirim.homework1.network.model.Movie

class ProfileMovieAdapter(private val movies: List<Movie>) :
    RecyclerView.Adapter<ProfileMovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: ItemProfileMovieBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemProfileMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.binding.movieTitle.text = movie.title
        // Use your preferred image loading library to load the poster
        // e.g., Glide or Coil
    }

    override fun getItemCount(): Int = movies.size
}
