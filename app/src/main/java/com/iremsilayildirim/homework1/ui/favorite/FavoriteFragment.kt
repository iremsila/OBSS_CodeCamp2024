package com.iremsilayildirim.homework1.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iremsilayildirim.homework1.data.database.DatabaseBuilder
import com.iremsilayildirim.homework1.databinding.FragmentFavoriteBinding
import com.iremsilayildirim.homework1.repository.FavoriteRepository

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteMovieAdapter: FavoriteMovieAdapter

    private val viewModel: FavoriteViewModel by viewModels {
        val database = DatabaseBuilder.getInstance(requireContext())
        val repository = FavoriteRepository(database)
        FavoriteViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteMovieAdapter = FavoriteMovieAdapter { movie ->
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToMovieDetailFragment(movie.id)
            findNavController().navigate(action)
        }

        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFavorites.adapter = favoriteMovieAdapter

        viewModel.allFavoriteMovies.observe(viewLifecycleOwner) { favoriteMovies ->
            favoriteMovies?.let {
                favoriteMovieAdapter.submitList(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
