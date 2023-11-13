package com.example.khuton2023.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.khuton2023.data.dao.ChatRoomListDao
import com.example.khuton2023.data.model.ChatRoomList
import com.example.khuton2023.data.model.MessageListConverter

@Database(entities = [ChatRoomList::class], version = 1)
@TypeConverters(MessageListConverter::class)
abstract class ChattingRoomListDatabase : RoomDatabase() {
    abstract fun chatRoomListDao(): ChatRoomListDao
}