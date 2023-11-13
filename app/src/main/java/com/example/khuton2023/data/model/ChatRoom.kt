package com.example.khuton2023.data.model

import android.graphics.Bitmap
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.io.Serializable

@Entity
data class ChatRoom(
    var studyMateName:String,
    var studyMateId: String,
    var messages: MutableList<Message>,
    var profileImage : Bitmap?,
    @PrimaryKey(autoGenerate = true)
    var uid: Int? =null
) : Serializable {
}