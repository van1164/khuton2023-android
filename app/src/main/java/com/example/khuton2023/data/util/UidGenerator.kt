package com.example.khuton2023.data.util

import java.util.UUID

class UidGenerator {
    fun generateUID(): String {
        val uid = UUID.randomUUID()
        return uid.toString()
    }
}