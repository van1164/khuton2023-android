package com.example.khuton2023.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.khuton2023.data.dao.ChatRoomDao
import com.example.khuton2023.data.model.ChatRoom
import com.example.khuton2023.data.model.MessageListConverter


@Database(entities = [ChatRoom::class], version = 1)
@TypeConverters(MessageListConverter::class)
abstract class ChattingData : RoomDatabase() {
    abstract fun chatRoomDao(): ChatRoomDao
}