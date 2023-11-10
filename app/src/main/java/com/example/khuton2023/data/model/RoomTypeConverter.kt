package com.example.khuton2023.data.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class RoomTypeConverter {

    @TypeConverter
    fun toString(uri: Uri?):String{
        return uri.toString()
    }

    // Bitmap -> ByteArray 변환
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