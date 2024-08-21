package com.arkan.takehomechallenge.data.source.network.services

import com.arkan.takehomechallenge.BuildConfig
import com.arkan.takehomechallenge.data.source.network.model.character.CharacterResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface THCApiService {
    @GET("character")
    suspend fun getAllCharacter(
        @Query("page") page: Int = 1,
    ): CharacterResponse

    @GET("character")
    suspend fun searchCharacter(
        @Query("page") page: Int = 1,
        @Query("name") name: String?,
    ): CharacterResponse

    companion object {
        @JvmStatic
        operator fun invoke(): THCApiService {
            val okHttpClient =
                OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build()
            val retrofit =
                Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
            return retrofit.create(THCApiService::class.java)
        }
    }
}
