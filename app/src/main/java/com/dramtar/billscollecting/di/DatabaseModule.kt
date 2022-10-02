package com.dramtar.billscollecting.di

import android.app.Application
import androidx.room.Room
import com.dramtar.billscollecting.data.local.BillDatabase
import com.dramtar.billscollecting.data.local.bill.BillDao
import com.dramtar.billscollecting.data.local.billtype.BillTypeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Application): BillDatabase {
        return Room.databaseBuilder(
            context,
            BillDatabase::class.java, "BillsDatabase.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideUserDao(database: BillDatabase): BillDao {
        return database.billDao
    }

    @Singleton
    @Provides
    fun provideDogDao(database: BillDatabase): BillTypeDao {
        return database.billTypeDao
    }
}
