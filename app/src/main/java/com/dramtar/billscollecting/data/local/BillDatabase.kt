package com.dramtar.billscollecting.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dramtar.billscollecting.data.local.bill.BillDao
import com.dramtar.billscollecting.data.local.bill.BillEntity
import com.dramtar.billscollecting.data.local.billtype.BillTypeDao
import com.dramtar.billscollecting.data.local.billtype.BillTypeEntity

@Database(entities = [BillEntity::class, BillTypeEntity::class], version = 1)
abstract class BillDatabase : RoomDatabase() {
    abstract val billDao: BillDao
    abstract val billTypeDao: BillTypeDao
}