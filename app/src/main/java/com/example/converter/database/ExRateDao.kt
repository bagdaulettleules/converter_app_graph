package com.example.converter.database

import androidx.lifecycle.LiveData
import androidx.room.*


/**
 * Created by Bagdaulet Tleules on 05.12.2021.
 * email: bagdaulettleules@gmail.com
 */
@Dao
interface ExRateDao {
    @Query("select * from ExRateEntity where date = :pDate")
    fun getAllRates(pDate: String): LiveData<List<ExRateEntity?>>

    @Query("select * from ExRateEntity where currFrom = :pCurrFrom and date = :pDate")
    fun getAllRates(pCurrFrom: String, pDate: String): LiveData<List<ExRateEntity?>>

    @Query("select * from ExRateEntity where currFrom = :pFrom and currTo = :pTo and date = :pDate")
    fun getRate(pFrom: String, pTo: String, pDate: String): LiveData<ExRateEntity?>

    @Query("select max(date) from ExRateEntity")
    fun getEffectiveDate(): LiveData<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRates(exRateEntity: List<ExRateEntity>)

    @Update
    fun updateRates(exRateEntity: ExRateEntity)
}