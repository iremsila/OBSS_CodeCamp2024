package com.iremsilayildirim.homework1.ui.detailpage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.iremsilayildirim.homework1.R
import com.iremsilayildirim.homework1.data.database.DatabaseBuilder
import com.iremsilayildirim.homework1.databinding.FragmentMovieDetailBinding
import com.iremsilayildirim.homework1.network.model.Movie
import com.iremsilayildirim.homework1.network.model.MovieDetail
import com.iremsilayildirim.homework1.repository.MovieDetailRepository
import com.iremsilayildirim.homework1.viewmodel.MovieDetailViewModel
import com.iremsilayildirim.homework1.viewmodel.MovieDetailViewModelFactory
import kotlinx.coroutines.launch

class MovieDetailFragment : Fragment() {

    private val args: MovieDetailFragmentArgs by navArgs()
    private val viewModel: MovieDetailViewModel by viewModels {
        val database = DatabaseBuilder.getInstance(requireContext())
        val repository = MovieDetailRepository(database)
        MovieDetailViewModelFactory(repository)
    }
    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieDetailAdapter: MovieDetailAdapter
    private lateinit var similarMoviesAdapter: SimilarMoviesAdapter
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.movieDetailRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.similarMoviesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val movieId = args.movieId

        viewModel.fetchMovieDetail(movieId)

        viewModel.movieDetail.observe(viewLifecycleOwner) { movieDetail ->
            movieDetail?.let {
                Glide.with(this).load("https://image.tmdb.org/t/p/w500${movieDetail.posterPath}")
                    .into(binding.moviePosterImageView)
                binding.movieTitleTextView.text = movieDetail.title
                binding.movieRuntimeTextView.text = "Duration: ${movieDetail.runtime} minutes"

                // RatingBar'ı voteAverage ile güncelle
                binding.movieRatingBar.rating = (movieDetail.voteAverage ?: 0.0).toFloat() / 2

                val details = listOf(
                    "Title" to movieDetail.title,
                    "Overview" to movieDetail.overview,
                    "Release Date" to movieDetail.release_date,
                    "Genres" to movieDetail.genres.joinToString(", ") { it.name }
                )
                movieDetailAdapter = MovieDetailAdapter(details)
                binding.movieDetailRecyclerView.adapter = movieDetailAdapter

                lifecycleScope.launch {
                    val favoriteMovie = viewModel.getFavoriteMovieById(movieId)
                    isFavorite = favoriteMovie != null
                    updateHeartIcon()
                }

                binding.heartIcon.setOnClickListener {
                    isFavorite = !isFavorite
                    updateHeartIcon()
                    handleFavorite(movieDetail)
                }

                if (movieDetail.genres.isNotEmpty()) {
                    viewModel.fetchMoviesByGenre(movieDetail.genres.first().id)
                }
            }
        }


        viewModel.similarMovies.observe(viewLifecycleOwner) { similarMovies ->
            similarMovies?.let {
                similarMoviesAdapter = SimilarMoviesAdapter(it) { movie ->
                    navigateToMovieDetail(movie)
                }
                binding.similarMoviesRecyclerView.adapter = similarMoviesAdapter
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateHeartIcon() {
        if (isFavorite) {
            binding.heartIcon.setImageResource(R.drawable.ic_favorite_filled)
        } else {
            binding.heartIcon.setImageResource(R.drawable.ic_favorite_outline)
        }
    }

    private fun handleFavorite(movieDetail: MovieDetail) {
        lifecycleScope.launch {
            if (isFavorite) {
                viewModel.addFavoriteMovie(movieDetail)
                Toast.makeText(context, "${movieDetail.title} added to favorites", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.removeFavoriteMovie(movieDetail.id)
                Toast.makeText(context, "${movieDetail.title} removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToMovieDetail(movie: Movie) {
        val action = MovieDetailFragmentDirections.actionMovieDetailFragmentSelf(movie.id)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
