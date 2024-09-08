package com.iremsilayildirim.homework1.ui.login

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.iremsilayildirim.homework1.data.dao.UserDao
import com.iremsilayildirim.homework1.data.database.DatabaseBuilder
import com.iremsilayildirim.homework1.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        // Veritabanı ve DAO başlatma
        val database = DatabaseBuilder.getInstance(requireContext())
        userDao = database.userDao()

        binding.signInButton.setOnClickListener {
            if (isInternetAvailable(requireContext())) {
                val email = binding.emailEditText.text.toString().trim()
                val password = binding.passwordEditText.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    signInUser(email, password)
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "No internet connection. Please check your connection.", Toast.LENGTH_LONG).show()
            }
        }

        binding.registerText.setOnClickListener {
            if (isInternetAvailable(requireContext())) {
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "No internet connection. Please check your connection.", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    private fun signInUser(email: String, password: String) {
        lifecycleScope.launch {
            val user = userDao.getUserByEmailAndPassword(email, password)
            if (user != null) {
                Toast.makeText(requireContext(), "Sign in successful", Toast.LENGTH_SHORT).show()
                // Giriş başarılı, kullanıcıyı MovieFragment'e yönlendirin
                val action = LoginFragmentDirections.actionLoginFragmentToMovieFragment()
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}
