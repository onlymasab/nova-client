package com.paandaaa.nova.android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.paandaaa.nova.android.data.local.entity.ConversationEntity


@Dao
interface ConversationDao {
    @Insert
    suspend fun insert(conversation: ConversationEntity)

    @Query("SELECT * FROM conversations ORDER BY timestamp DESC")
    suspend fun getAll(): List<ConversationEntity>
}
