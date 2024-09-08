package com.iremsilayildirim.homework1.ui.listpage

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iremsilayildirim.homework1.R
import com.iremsilayildirim.homework1.databinding.FragmentMovieBinding
import com.iremsilayildirim.homework1.network.RetrofitInstance
import com.iremsilayildirim.homework1.network.model.Movie
import com.iremsilayildirim.homework1.network.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieFragment : Fragment() {

    private val viewModel: MovieViewModel by activityViewModels()
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieSearchAdapter: MovieSearchAdapter
    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private var movieList: MutableList<Movie> = mutableListOf()
    private var currentPage = 1
    private var isLoading = false
    private var lastQuery: String = ""
    private var layoutState = MovieAdapter.LayoutState.LIST

    private var recyclerViewState: Parcelable? = null

    private val apiService: ApiService by lazy {
        RetrofitInstance.api
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        val recyclerView = binding.recyclerView
        val layoutSelectorSpinner = binding.layoutSelectorSpinner
        val editTextSearch = binding.editTextSearch

        // İkonlar ve etiketler
        val icons = listOf(R.drawable.ic_list_view, R.drawable.ic_grid_view_2, R.drawable.ic_grid_view_3)
        val labels = listOf("1-Column", "2-Column", "3-Column")

        // IconSpinnerAdapter oluşturun ve spinner'a atayın
        val spinnerAdapter = IconSpinnerAdapter(requireContext(), icons, labels)
        layoutSelectorSpinner.adapter = spinnerAdapter

        layoutSelectorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> setLayoutManager(MovieAdapter.LayoutState.LIST)
                    1 -> setLayoutManager(MovieAdapter.LayoutState.GRID_2)
                    2 -> setLayoutManager(MovieAdapter.LayoutState.GRID_3)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Adapter ve navigasyon ayarları
        movieAdapter = MovieAdapter { movie ->
            saveRecyclerViewState()
            val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(movie.id)
            findNavController().navigate(action)
        }
        movieSearchAdapter = MovieSearchAdapter(movieList) { movie ->
            findNavController().navigate(MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(movie.id))
        }

        recyclerView.adapter = movieAdapter

        viewModel.movieList.observe(viewLifecycleOwner) { movies ->
            movieAdapter.submitList(movies.toList())
            isLoading = false
            restoreRecyclerViewState()
            setLayoutManager(layoutState)
        }

        if (viewModel.movieList.value.isNullOrEmpty()) {
            viewModel.fetchMovies(currentPage)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                applyParallaxEffect(recyclerView)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!isLoading && layoutManager.findLastCompletelyVisibleItemPosition() == movieAdapter.itemCount - 1) {
                    isLoading = true
                    currentPage++
                    viewModel.fetchMovies(currentPage)
                }
            }
        })

        editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    lastQuery = query
                    currentPage = 1
                    movieList.clear()
                    searchMovies(query)

                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = movieSearchAdapter
                } else {
                    // Arama kutusu temizlenirse
                    recyclerView.adapter = movieAdapter
                    viewModel.movieList.value?.let {
                        movieAdapter.submitList(it.toList())
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (editTextSearch.text.isNotEmpty()) {
                        // Arama kutusu doluysa ve geri tuşuna basıldığında
                        editTextSearch.text.clear() // Arama kutusunu temizle
                        recyclerView.adapter = movieAdapter
                        viewModel.movieList.value?.let {
                            movieAdapter.submitList(it.toList())
                        }
                        setLayoutManager(layoutState) // Layout state'i geri yükle
                        restoreRecyclerViewState() // Önceki RecyclerView durumunu geri yükle
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            }
        )
    }

    private fun showLoading() {
        binding.loadingLayout.bringToFront()
        binding.loadingLayout.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingLayout.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun searchMovies(query: String) {
        if (query.isEmpty()) return

        showLoading() // Show loading before starting the request
        isLoading = true
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    apiService.searchMovies(query, currentPage)
                }

                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()?.results ?: emptyList()
                    Log.d("MovieFragment", "Movies fetched: ${movies.size}")

                    if (currentPage == 1) {
                        movieList.clear()
                    }
                    movieList.addAll(movies)
                    movieSearchAdapter.updateMovies(movieList)

                    binding.recyclerView.visibility = View.VISIBLE
                    binding.recyclerView.adapter = movieSearchAdapter
                } else {
                    Log.d("MovieFragment", "Response not successful: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MovieFragment", "Error searching movies: $e")
            } finally {
                hideLoading() // Hide loading after request completes
                isLoading = false
            }
        }
    }

    private fun setLayoutManager(state: MovieAdapter.LayoutState) {
        when (state) {
            MovieAdapter.LayoutState.LIST -> {
                binding.recyclerView.layoutManager = LinearLayoutManager(context)
            }
            MovieAdapter.LayoutState.GRID_2 -> {
                binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
            }
            MovieAdapter.LayoutState.GRID_3 -> {
                binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
            }
        }
        layoutState = state
        movieAdapter.setLayoutState(state)
        restoreRecyclerViewState()
    }

    private fun saveRecyclerViewState() {
        recyclerViewState = binding.recyclerView.layoutManager?.onSaveInstanceState()
    }

    private fun restoreRecyclerViewState() {
        recyclerViewState?.let { state ->
            binding.recyclerView.layoutManager?.onRestoreInstanceState(state)
            recyclerViewState = null
        }
    }

    override fun onPause() {
        super.onPause()
        saveRecyclerViewState() // Fragment duraklatıldığında durumu kaydet
    }

    override fun onResume() {
        super.onResume()
        restoreRecyclerViewState() // Fragment devam ettirildiğinde durumu geri yükle
        applyParallaxEffect(binding.recyclerView)
    }

    private fun applyParallaxEffect(recyclerView: RecyclerView) {
        val childCount = recyclerView.childCount
        for (i in 0 until childCount) {
            val child = recyclerView.getChildAt(i)
            val holder = recyclerView.getChildViewHolder(child)

            // Sadece MovieAdapter için parallax etkisini uygula
            if (holder is MovieAdapter.MovieViewHolder) {
                val itemHeight = child.height
                val viewTop = child.top
                val recyclerViewHeight = recyclerView.height

                val centerY = recyclerViewHeight / 2f
                val childCenterY = (viewTop + itemHeight / 2f)
                val distanceFromCenter = centerY - childCenterY

                val maxTranslation = itemHeight * 0.3f
                val translationY = (distanceFromCenter / recyclerViewHeight) * maxTranslation

                holder.itemView.findViewById<ImageView>(R.id.moviePoster).translationY = translationY
            }
        }
    }
}

class IconSpinnerAdapter(
    context: Context,
    private val icons: List<Int>,
    private val labels: List<String>
) : ArrayAdapter<String>(context, R.layout.spinner_item, labels) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false)

        val icon = view.findViewById<ImageView>(R.id.spinnerIcon)
        val label = view.findViewById<TextView>(R.id.spinnerText)

        icon.setImageResource(icons[position])
        label.text = labels[position]

        return view
    }
}
