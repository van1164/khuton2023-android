package com.example.khuton2023.data.model


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


data class StudyMate(
    val name: String="",
    val year: Int=1999,
    val month: Int=1,
    val day: Int=1,
    val mbti: Mbti=Mbti.ISFP,
    val profileImageUri: String? = null,
    val uid: String? =null
)
