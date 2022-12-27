package com.dramtar.billscollecting.data.local.bill

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {
    @Insert(onConflict = REPLACE)
    suspend fun saveBill(vararg billEntity: BillEntity)

    @Query("SELECT * FROM bill_table WHERE timestamp > :start AND timestamp < :end ORDER BY timestamp DESC")
    fun getBills(start: Long, end: Long): Flow<List<BillEntity>>

    @Query("DELETE FROM bill_table WHERE id = :id")
    suspend fun deleteBill(id: Int)

    @Query("SELECT * FROM bill_table ORDER BY timestamp DESC")
    suspend fun getAllBills(): List<BillEntity>

    @Query("SELECT * FROM bill_table WHERE billTypeId = :typeID ORDER BY timestamp")
    suspend fun getAllBillsByTypeID(typeID: String): List<BillEntity>
}