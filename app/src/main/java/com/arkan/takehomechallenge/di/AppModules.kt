package com.arkan.takehomechallenge.di

import com.arkan.takehomechallenge.data.datasource.CharacterApiDataSource
import com.arkan.takehomechallenge.data.datasource.CharacterDataSource
import com.arkan.takehomechallenge.data.datasource.FavoriteDataSource
import com.arkan.takehomechallenge.data.datasource.FavoriteDatabaseDataSource
import com.arkan.takehomechallenge.data.repository.CharacterRepository
import com.arkan.takehomechallenge.data.repository.CharacterRepositoryImpl
import com.arkan.takehomechallenge.data.repository.FavoriteRepository
import com.arkan.takehomechallenge.data.repository.FavoriteRepositoryImpl
import com.arkan.takehomechallenge.data.source.local.AppDatabase
import com.arkan.takehomechallenge.data.source.local.dao.FavoriteDao
import com.arkan.takehomechallenge.data.source.network.services.THCApiService
import com.arkan.takehomechallenge.presentation.detail.DetailViewModel
import com.arkan.takehomechallenge.presentation.favorite.FavoriteViewModel
import com.arkan.takehomechallenge.presentation.main.MainViewModel
import com.arkan.takehomechallenge.presentation.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object AppModules {
    private val localModule =
        module {
            single<AppDatabase> { AppDatabase.getInstance(androidContext()) }
            single<FavoriteDao> { get<AppDatabase>().favoriteDao() }
        }

    private val apiModule =
        module {
            single<THCApiService> { THCApiService.invoke() }
        }

    private val dataSource =
        module {
            single<CharacterDataSource> { CharacterApiDataSource(get()) }
            single<FavoriteDataSource> { FavoriteDatabaseDataSource(get()) }
        }

    private val repository =
        module {
            single<CharacterRepository> { CharacterRepositoryImpl(get()) }
            single<FavoriteRepository> { FavoriteRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::MainViewModel)
            viewModelOf(::DetailViewModel)
            viewModelOf(::SearchViewModel)
            viewModelOf(::FavoriteViewModel)
        }

    val modules =
        listOf(
            localModule,
            apiModule,
            dataSource,
            repository,
            viewModelModule,
        )
}
