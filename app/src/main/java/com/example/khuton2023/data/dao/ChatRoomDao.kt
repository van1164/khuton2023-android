package com.example.khuton2023.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.khuton2023.data.model.ChatRoom
import com.example.khuton2023.data.model.Message

@Dao
interface ChatRoomDao {
    @Query("SELECT * FROM chatroom")
    fun getAll(): List<ChatRoom>

    @Query("SELECT * FROM chatroom WHERE studyMateId is :studyMateId")
    fun findByStudyMateId(vararg studyMateId:String): ChatRoom

    @Insert
    fun insert(vararg chatRoom: ChatRoom)

    @Update
    fun update(vararg chatRoom: ChatRoom)
}