package com.example.converter.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.converter.database.ExRateEntity


/**
 * Created by Bagdaulet Tleules on 05.12.2021.
 * email: bagdaulettleules@gmail.com
 */
@Database(entities = [ExRateEntity::class], version = 2, exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class Database : RoomDatabase() {
    abstract fun exRateDao(): ExRateDao
}
