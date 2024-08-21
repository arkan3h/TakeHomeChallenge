package com.arkan.takehomechallenge.di

import com.arkan.takehomechallenge.data.datasource.CharacterApiDataSource
import com.arkan.takehomechallenge.data.datasource.CharacterDataSource
import com.arkan.takehomechallenge.data.repository.CharacterRepository
import com.arkan.takehomechallenge.data.repository.CharacterRepositoryImpl
import com.arkan.takehomechallenge.data.source.network.services.THCApiService
import com.arkan.takehomechallenge.presentation.detail.DetailViewModel
import com.arkan.takehomechallenge.presentation.main.MainViewModel
import com.arkan.takehomechallenge.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

object AppModules {
    private val apiModule =
        module {
            single<THCApiService> { THCApiService.invoke() }
        }

    private val dataSource =
        module {
            single<CharacterDataSource> { CharacterApiDataSource(get()) }
        }

    private val repository =
        module {
            single<CharacterRepository> { CharacterRepositoryImpl(get()) }
        }

    private val viewModelModule =
        module {
            viewModelOf(::MainViewModel)
            viewModelOf(::DetailViewModel)
            viewModelOf(::SearchViewModel)
        }

    val modules =
        listOf(
            apiModule,
            dataSource,
            repository,
            viewModelModule,
        )
}
