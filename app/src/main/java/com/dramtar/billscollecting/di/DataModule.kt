package com.dramtar.billscollecting.di

import com.dramtar.billscollecting.data.repository.BillTypesRepositoryImpl
import com.dramtar.billscollecting.data.repository.BillsRepositoryImpl
import com.dramtar.billscollecting.domain.BillTypesRepository
import com.dramtar.billscollecting.domain.BillsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindBillsRepository(billsRepositoryImpl: BillsRepositoryImpl): BillsRepository
    @Binds
    abstract fun bindBillTypesRepository(billTypesRepositoryImpl: BillTypesRepositoryImpl): BillTypesRepository
}