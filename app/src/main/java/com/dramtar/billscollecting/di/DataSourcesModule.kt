package com.dramtar.billscollecting.di

import com.dramtar.billscollecting.data.local.BillTypesLocalDataSource
import com.dramtar.billscollecting.data.local.BillTypesLocalDataSourceImpl
import com.dramtar.billscollecting.data.local.BillsLocalDataSource
import com.dramtar.billscollecting.data.local.BillsLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourcesModule {
    @Binds
    abstract fun bindBillsLocalDataSource(billsLocalDataSourceImpl: BillsLocalDataSourceImpl): BillsLocalDataSource

    @Binds
    abstract fun bindBillTypesLocalDataSource(billTypesLocalDataSourceImpl: BillTypesLocalDataSourceImpl): BillTypesLocalDataSource
}
