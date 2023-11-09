package com.example.khuton2023.data.model

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


enum class Mbti(name:String) {
    ISFP("ISFP"),
    ISFJ("ISFJ"),
    ISTP("ISTP"),
    ISTJ("ISTJ"),
    INFP("INFP"),
    INFJ("INFJ"),
    INTP("INTP"),
    INTJ("INTJ"),
    ESFP("ESFP"),
    ESFJ("ESFJ"),
    ESTP("ESTP"),
    ESTJ("ESTJ"),
    ENFP("ENFP"),
    ENFJ("ENFJ"),
    ENTP("ENTP"),
    ENTJ("ENTJ"),
}

@Entity
data class StudyMate(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "year") val year: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "mbti") val mbti: Mbti,
    @ColumnInfo(name = "profileImage") val profileImage: Bitmap?,

    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
)
