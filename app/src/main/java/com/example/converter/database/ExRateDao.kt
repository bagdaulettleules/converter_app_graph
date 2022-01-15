package com.example.converter.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.converter.model.DatabaseModel
import java.util.*


/**
 * Created by Bagdaulet Tleules on 05.12.2021.
 * email: bagdaulettleules@gmail.com
 */
@Dao
interface ExRateDao {
    @Query("select * from DatabaseModel where date = :pDate")
    fun getAllRates(pDate: String): LiveData<List<DatabaseModel?>>

    @Query("select * from DatabaseModel where currFrom = :pCurrFrom and date = :pDate")
    fun getAllRates(pCurrFrom: String, pDate: String): LiveData<List<DatabaseModel?>>

    @Query("select * from DatabaseModel where currFrom = :pFrom and currTo = :pTo and date = :pDate")
    fun getRate(pFrom: String, pTo: String, pDate: String): LiveData<DatabaseModel?>

    @Query("select max(date) from DatabaseModel")
    fun getEffectiveDate(): LiveData<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRates(databaseModel: List<DatabaseModel>)

    @Update
    fun updateRates(databaseModel: DatabaseModel)
}