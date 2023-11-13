package com.example.khuton2023.data.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Message(
    val studyMateId: String,
    var message: String = "",
    val oppo:Boolean,
    var confirmed:Boolean=false,
    @PrimaryKey(autoGenerate = true)
    val uid: Int? =null
) : Serializable {
}

