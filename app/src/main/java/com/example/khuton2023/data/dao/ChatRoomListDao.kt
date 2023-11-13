package com.example.khuton2023.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.khuton2023.data.model.ChatRoom
import com.example.khuton2023.data.model.ChatRoomList

@Dao
interface ChatRoomListDao {
    @Query("SELECT * FROM chatroomlist")
    fun getAll(): List<ChatRoomList>

    @Insert
    fun insert(vararg chatRoomList: ChatRoomList)
}