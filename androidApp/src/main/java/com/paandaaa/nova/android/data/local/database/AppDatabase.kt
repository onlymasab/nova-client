package com.paandaaa.nova.android.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paandaaa.nova.android.data.local.dao.ConversationDao
import com.paandaaa.nova.android.data.local.entity.ConversationEntity

@Database(entities = [ConversationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
}