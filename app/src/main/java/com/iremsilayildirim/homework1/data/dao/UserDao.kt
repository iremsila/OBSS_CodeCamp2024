package com.iremsilayildirim.homework1.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.iremsilayildirim.homework1.data.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getUserById(id: Int): User?

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?
}
