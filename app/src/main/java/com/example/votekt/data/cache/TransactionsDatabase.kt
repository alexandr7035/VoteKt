package com.example.votekt.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        TransactionEntity::class,
        ProposalEntity::class,
    ],
    version = 1
)
abstract class TransactionsDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun proposalsDao(): ProposalsDao
}