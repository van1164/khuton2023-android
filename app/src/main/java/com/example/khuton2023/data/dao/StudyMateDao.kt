package com.example.khuton2023.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.khuton2023.data.model.StudyMate

@Dao
interface StudyMateDao {
    @Query("SELECT * FROM studymate")
    fun getAll(): List<StudyMate>

    @Query("SELECT * FROM studymate WHERE id == :findId")
    fun getStudyMate(findId : String): StudyMate

    @Insert
    fun insert(vararg studyMate: StudyMate)
}