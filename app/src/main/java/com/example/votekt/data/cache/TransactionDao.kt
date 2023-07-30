package com.example.votekt.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY dateSent DESC")
    fun getTransactions(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE hash = :hash")
    suspend fun getTransactionByHash(hash: String): TransactionEntity?

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions")
    suspend fun clearTransactionHistory()
}