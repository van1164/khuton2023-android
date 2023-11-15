package com.example.khuton2023.data.model

import java.io.Serializable

data class AppUser(val name:String?="",
                val uid:String?="",
                val email:String?=""): Serializable {
}