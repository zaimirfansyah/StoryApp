package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import com.dicoding.picodiploma.loginwithanimation.api.ApiService
import com.dicoding.picodiploma.loginwithanimation.api.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.api.response.LoginResult
import com.dicoding.picodiploma.loginwithanimation.api.response.RegisterResponse

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel?> {
        return userPreference.getSession()

    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun registerUser(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)

    }

    suspend fun loginUser(email: String, password: String): Pair<LoginResponse, LoginResult?> {
        val response = apiService.login(email, password)
        return Pair(response, response.loginResult)
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}