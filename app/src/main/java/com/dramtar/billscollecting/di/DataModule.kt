package com.dramtar.billscollecting.di

import com.dramtar.billscollecting.data.repository.RepositoryImpl
import com.dramtar.billscollecting.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindRepository(repositoryImpl: RepositoryImpl): Repository
}