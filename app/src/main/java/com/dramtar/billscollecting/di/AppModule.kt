package com.dramtar.billscollecting.di

import com.dramtar.billscollecting.domain.BillTypesRepository
import com.dramtar.billscollecting.domain.BillsRepository
import com.dramtar.billscollecting.domain.use_case.GetAllBills
import com.dramtar.billscollecting.domain.use_case.GetBills
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGetAllBillsUseCases(
        billsRepository: BillsRepository,
        billTypesRepository: BillTypesRepository
    ): GetAllBills {
        return GetAllBills(
            billsRepository = billsRepository,
            billTypesRepository = billTypesRepository
        )
    }

    @Provides
    @Singleton
    fun provideGetBillsUseCases(
        billsRepository: BillsRepository,
        billTypesRepository: BillTypesRepository
    ): GetBills {
        return GetBills(
            billsRepository = billsRepository,
            billTypesRepository = billTypesRepository
        )
    }
}