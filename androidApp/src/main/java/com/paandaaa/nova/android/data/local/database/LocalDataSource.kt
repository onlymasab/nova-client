package com.paandaaa.nova.android.data.local.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//class LocalDataSource @Inject constructor(
//    private val conversationDao: ConversationDao
//) {
//    suspend fun saveConversation(query: String, response: String) {
//        conversationDao.insert(ConversationEntity(query = query, response = response))
//    }
//
//    fun getConversations(): Flow<List<ConversationEntity>> {
//        return conversationDao.getAllConversations()
//    }
//}