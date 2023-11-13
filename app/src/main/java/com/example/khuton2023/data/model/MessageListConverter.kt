package com.example.khuton2023.data.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.io.ByteArrayOutputStream

class MessageListConverter {
    @TypeConverter
    fun listToJson(value: MutableList<Message>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): MutableList<Message> {
        return Gson().fromJson(value,Array<Message>::class.java).toMutableList()
    }

    @TypeConverter
    fun toByteArray(bitmap : Bitmap?) : ByteArray?{
        val outputStream = ByteArrayOutputStream()
        return if (bitmap != null){
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.toByteArray()
        } else{
            null
        }

    }

    // ByteArray -> Bitmap 변환
    @TypeConverter
    fun toBitmap(bytes : ByteArray?) : Bitmap?{
        return if(bytes != null){
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } else{
            null
        }
    }
}