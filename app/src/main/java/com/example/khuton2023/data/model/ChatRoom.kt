package com.example.khuton2023.data.model

import java.io.Serializable

data class ChatRoom(
    val users: Map<String, StudyMate> = HashMap(),
    var messages: Map<String,Message>? = HashMap()
) : Serializable {
}