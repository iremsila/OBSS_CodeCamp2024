package com.iremsilayildirim.homework1.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iremsilayildirim.homework1.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using data binding
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(requireContext())).get(ProfileViewModel::class.java)

        // Bind ViewModel to layout
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Load the user's profile data
        val userId = 1 // Replace with the actual user ID
        viewModel.loadUserProfile(userId)

        return binding.root
    }

    private fun setupProfileUI() {
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.profileName.text = name
        }

        viewModel.userEmail.observe(viewLifecycleOwner) { email ->
            binding.profileEmail.text = email
        }

        binding.editProfileButton.setOnClickListener {
            // Handle edit profile action
        }
    }
}
