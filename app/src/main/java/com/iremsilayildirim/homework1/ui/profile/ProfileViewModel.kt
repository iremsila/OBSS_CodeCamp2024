package com.iremsilayildirim.homework1.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iremsilayildirim.homework1.data.dao.ProfileDao
import com.iremsilayildirim.homework1.data.dao.UserDao
import com.iremsilayildirim.homework1.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userDao: UserDao,
    private val profileDao: ProfileDao
) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String> get() = _userEmail

    fun loadUserProfile(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) { // Dispatchers.IO ile arka plan iş parçacığında çalıştırılır
            val user: User? = userDao.getUserById(userId)
            user?.let {
                _userName.postValue("${it.firstName} ${it.lastName}")
                _userEmail.postValue(it.email)
            }
        }
    }
}
