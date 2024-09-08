package com.iremsilayildirim.homework1.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.iremsilayildirim.homework1.R
import com.iremsilayildirim.homework1.databinding.FragmentSearchBinding
import com.iremsilayildirim.homework1.network.RetrofitInstance
import com.iremsilayildirim.homework1.network.model.Movie
import com.iremsilayildirim.homework1.network.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by activityViewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchMovieAdapter: SearchMovieAdapter
    private var movieList: MutableList<Movie> = mutableListOf()
    private var currentPage = 1
    private var isLoading = false
    private var lastQuery: String = ""

    private val apiService: ApiService by lazy {
        RetrofitInstance.api
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchMovieAdapter = SearchMovieAdapter(movieList) { movie ->
            findNavController().navigate(SearchFragmentDirections.actionSearchToMovieDetail(movie.id))
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = searchMovieAdapter

        searchViewModel.horrorMovie.observe(viewLifecycleOwner) { movie ->
            movie?.let { updateHorrorMovieCard(it) }
        }

        searchViewModel.animationMovie.observe(viewLifecycleOwner) { movie ->
            movie?.let { updateAnimationMovieCard(it) }
        }

        searchViewModel.adventureMovie.observe(viewLifecycleOwner) { movie ->
            movie?.let { updateAdventureMovieCard(it) }
        }

        searchViewModel.comedyMovie.observe(viewLifecycleOwner) { movie ->
            movie?.let { updateComedyMovieCard(it) }
        }

        if (searchViewModel.areMoviesLoaded()) {
            // Movies are already loaded, UI will update automatically via LiveData observers
        } else {
            loadMovies()
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    lastQuery = query
                    currentPage = 1
                    movieList.clear()
                    searchMovies(query)
                    binding.gridLayout.visibility = View.GONE
                    binding.textViewSuggestionTitle.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                } else {
                    binding.gridLayout.visibility = View.VISIBLE
                    binding.textViewSuggestionTitle.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.recyclerView.visibility == View.VISIBLE) {
                        binding.gridLayout.visibility = View.VISIBLE
                        binding.textViewSuggestionTitle.visibility = View.VISIBLE
                        binding.recyclerView.visibility = View.GONE
                        binding.editTextSearch.text.clear()
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
        )
    }

    private fun loadMovies() {
        loadHorrorMovie()
        loadAnimationMovie()
        loadAdventureMovie()
        loadComedyMovie()
    }

    private fun loadHorrorMovie() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getMoviesByCategory(27) // Horror genre ID
                }

                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()?.results?.shuffled()
                    movies?.firstOrNull()?.let { searchViewModel.setHorrorMovie(it) }
                } else {
                    Log.d("SearchFragment", "Failed to load horror movies: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SearchFragment", "Error loading horror movies: $e")
            }
        }
    }

    private fun loadAnimationMovie() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getMoviesByCategory(16) // Animation genre ID
                }

                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()?.results?.shuffled()
                    movies?.firstOrNull()?.let { searchViewModel.setAnimationMovie(it) }
                } else {
                    Log.d("SearchFragment", "Failed to load animation movies: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SearchFragment", "Error loading animation movies: $e")
            }
        }
    }

    private fun loadAdventureMovie() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getMoviesByCategory(12) // Adventure genre ID
                }

                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()?.results?.shuffled()
                    movies?.firstOrNull()?.let { searchViewModel.setAdventureMovie(it) }
                } else {
                    Log.d("SearchFragment", "Failed to load adventure movies: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SearchFragment", "Error loading adventure movies: $e")
            }
        }
    }

    private fun loadComedyMovie() {
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.getMoviesByCategory(35) // Comedy genre ID
                }

                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()?.results?.shuffled()
                    movies?.firstOrNull()?.let { searchViewModel.setComedyMovie(it) }
                } else {
                    Log.d("SearchFragment", "Failed to load comedy movies: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SearchFragment", "Error loading comedy movies: $e")
            }
        }
    }

    private fun updateHorrorMovieCard(movie: Movie) {
        binding.genreTextViewHorror.text = "Horror"
        binding.titleTextViewHorror.text = movie.title

        // Show loading
        binding.progressBarLoadingHorror.visibility = View.VISIBLE

        binding.posterImageViewHorror.load("https://image.tmdb.org/t/p/w500/" + movie.poster_path) {
            crossfade(true)
            placeholder(R.drawable.placeholder_image)
            listener(
                onSuccess = { _, _ -> binding.progressBarLoadingHorror.visibility = View.GONE },
                onError = { _, _ -> binding.progressBarLoadingHorror.visibility = View.GONE }
            )
        }

        binding.horrorMovieCard.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchToMovieDetail(movie.id))
        }
    }

    private fun updateAnimationMovieCard(movie: Movie) {
        binding.genreTextViewAnimation.text = "Animation"
        binding.titleTextViewAnimation.text = movie.title

        // Show loading
        binding.progressBarLoadingAnimation.visibility = View.VISIBLE

        binding.posterImageViewAnimation.load("https://image.tmdb.org/t/p/w500/" + movie.poster_path) {
            crossfade(true)
            placeholder(R.drawable.placeholder_image)
            listener(
                onSuccess = { _, _ -> binding.progressBarLoadingAnimation.visibility = View.GONE },
                onError = { _, _ -> binding.progressBarLoadingAnimation.visibility = View.GONE }
            )
        }

        binding.animationMovieCard.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchToMovieDetail(movie.id))
        }
    }

    private fun updateAdventureMovieCard(movie: Movie) {
        binding.genreTextViewAdventure.text = "Adventure"
        binding.titleTextViewAdventure.text = movie.title

        // Show loading
        binding.progressBarLoadingAdventure.visibility = View.VISIBLE

        binding.posterImageViewAdventure.load("https://image.tmdb.org/t/p/w500/" + movie.poster_path) {
            crossfade(true)
            placeholder(R.drawable.placeholder_image)
            listener(
                onSuccess = { _, _ -> binding.progressBarLoadingAdventure.visibility = View.GONE },
                onError = { _, _ -> binding.progressBarLoadingAdventure.visibility = View.GONE }
            )
        }

        binding.adventureMovieCard.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchToMovieDetail(movie.id))
        }
    }

    private fun updateComedyMovieCard(movie: Movie) {
        binding.genreTextViewComedy.text = "Comedy"
        binding.titleTextViewComedy.text = movie.title

        // Show loading
        binding.progressBarLoadingComedy.visibility = View.VISIBLE

        binding.posterImageViewComedy.load("https://image.tmdb.org/t/p/w500/" + movie.poster_path) {
            crossfade(true)
            placeholder(R.drawable.placeholder_image)
            listener(
                onSuccess = { _, _ -> binding.progressBarLoadingComedy.visibility = View.GONE },
                onError = { _, _ -> binding.progressBarLoadingComedy.visibility = View.GONE }
            )
        }

        binding.comedyMovieCard.setOnClickListener {
            findNavController().navigate(SearchFragmentDirections.actionSearchToMovieDetail(movie.id))
        }
    }

    private fun searchMovies(query: String) {
        if (query.isEmpty()) return

        isLoading = true
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.searchMovies(query, currentPage)
                }

                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()?.results ?: emptyList()
                    Log.d("SearchFragment", "Movies fetched: ${movies.size}")

                    if (currentPage == 1) {
                        movieList.clear()
                    }
                    movieList.addAll(movies)
                    searchMovieAdapter.updateMovies(movieList)

                    binding.recyclerView.visibility = View.VISIBLE
                    binding.gridLayout.visibility = View.GONE
                    binding.textViewSuggestionTitle.visibility = View.GONE
                } else {
                    Log.d("SearchFragment", "Response not successful: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("SearchFragment", "Error searching movies: $e")
            } finally {
                isLoading = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
