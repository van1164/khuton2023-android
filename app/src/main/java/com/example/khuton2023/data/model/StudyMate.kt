package com.example.khuton2023.data.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


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
    var name: String="",
    var year: Int=1999,
    var month: Int=1,
    var day: Int=1,
    var mbti: Mbti=Mbti.ISFP,
    var profileImage: Bitmap? = null,
    var uid: String? =null,

    @PrimaryKey(autoGenerate = true)
    var id : Int? =null

): Serializable
