package by.alexandr7035.votekt.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.alexandr7035.votekt.data.cache.adapters.NumberTypeConvertors
import by.alexandr7035.votekt.data.cache.adapters.WeiTypeConvertor

@Database(
    entities = [
        TransactionEntity::class,
        ProposalEntity::class,
    ],
    version = 1
)
@TypeConverters(WeiTypeConvertor::class, NumberTypeConvertors::class)
abstract class TransactionsDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun proposalsDao(): ProposalsDao
}
