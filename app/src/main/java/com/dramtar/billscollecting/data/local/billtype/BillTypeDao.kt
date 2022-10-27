package com.dramtar.billscollecting.data.local.billtype

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface BillTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveBillType(vararg billTypeEntity: BillTypeEntity)

    @Query("SELECT * FROM bill_type_table ORDER BY priority DESC")
    fun getBillTypes(): Flow<List<BillTypeEntity>>

    @Query("SELECT * FROM bill_type_table WHERE id = :id")
    suspend fun getBillTypeById(id: String): BillTypeEntity?

    @Update
    suspend fun updateBillType(billTypeEntity: BillTypeEntity)

    @Query("DELETE FROM bill_type_table WHERE id = :id")
    suspend fun deleteBillType(id: String)
}