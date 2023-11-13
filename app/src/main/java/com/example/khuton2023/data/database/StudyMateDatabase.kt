package com.example.khuton2023.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameTable
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.khuton2023.data.dao.StudyMateDao
import com.example.khuton2023.data.model.RoomTypeConverter
import com.example.khuton2023.data.model.StudyMate


@Database(entities = [StudyMate::class], version = 1)
@TypeConverters(RoomTypeConverter::class)
abstract class StudyMateData : RoomDatabase(){
    abstract fun studyMateDao():StudyMateDao

}