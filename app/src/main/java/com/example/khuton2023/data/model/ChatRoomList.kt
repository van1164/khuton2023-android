package com.example.khuton2023.data.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatRoomList(
    var name : String,
    var studyMateId: String,
    var lastMessage : String,
    var isChecked : Boolean,
    var profileImage : Bitmap?,
    @PrimaryKey(autoGenerate = true)
    var uid: Int? =null
)

