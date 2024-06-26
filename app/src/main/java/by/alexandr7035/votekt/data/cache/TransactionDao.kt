package by.alexandr7035.votekt.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import by.alexandr7035.votekt.domain.model.transactions.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY dateSent DESC")
    fun getTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun cacheTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE hash = :hash")
    suspend fun getTransactionByHash(hash: String): TransactionEntity?

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE status != 'PENDING'")
    suspend fun clearTransactionHistory()

    @Query("SELECT type FROM transactions WHERE hash = :transactionHash")
    fun getTransactionType(transactionHash: String): TransactionType?
}
