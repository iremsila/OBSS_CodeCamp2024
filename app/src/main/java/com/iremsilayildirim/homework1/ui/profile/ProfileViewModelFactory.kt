package com.iremsilayildirim.homework1.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iremsilayildirim.homework1.data.database.DatabaseBuilder

class ProfileViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            val database = DatabaseBuilder.getInstance(context)
            val userDao = database.userDao()
            val profileDao = database.profileDao()
            return ProfileViewModel(userDao, profileDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
