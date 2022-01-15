package com.example.converter.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.converter.model.DatabaseModel


/**
 * Created by Bagdaulet Tleules on 05.12.2021.
 * email: bagdaulettleules@gmail.com
 */
@Database(entities = [DatabaseModel::class], version = 2, exportSchema = false)
@TypeConverters(ExRateTypeConverter::class)
abstract class ExRatesDatabase : RoomDatabase() {
    abstract fun exRateDao(): ExRateDao
}
