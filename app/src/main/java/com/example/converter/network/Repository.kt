package com.example.converter.network

import android.content.Context
import androidx.room.Room
import com.example.converter.database.Database
import com.example.converter.database.ExRateEntity
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


/**
 * Created by Bagdaulet Tleules on 05.12.2021.
 * email: bagdaulettleules@gmail.com
 */
private const val DATABASE_NAME = "converter_database"

class Repository private constructor(context: Context) {
    private val BASE_URL: String = "http://api.exchangeratesapi.io/v1/"
    private val ACCESS_KEY: String = "1b2cf47776fa684ae56fe45e4088866a"

    private val network = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
        .create(ExRateApi::class.java)

    private val database =
        Room.databaseBuilder(context, Database::class.java, DATABASE_NAME).build()
    private val currencyDao = database.exRateDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getExRatesFromNetwork(): Single<ResponseModel> = network.getLatest(ACCESS_KEY)

    fun getAllRatesFromDatabase(date: String) = currencyDao.getAllRates(date)

    fun getAllRatesFromDatabase(currFrom: String, date: String) =
        currencyDao.getAllRates(currFrom, date)

    fun getRateFromDatabase(from: String, to: String, date: String) =
        currencyDao.getRate(from, to, date)

    fun addExRates(list: List<ExRateEntity>) = executor.execute { currencyDao.insertRates(list) }

    fun updateRates(exRateEntity: ExRateEntity) =
        executor.execute { currencyDao.updateRates(exRateEntity) }

    fun getEffectiveDate() = currencyDao.getEffectiveDate()

    companion object {
        private var INSTANCE: Repository? = null

        fun newInstance(context: Context): Repository? {
            if (INSTANCE == null) {
                INSTANCE = Repository(context)
            }

            return INSTANCE
        }

        fun get(): Repository {
            return INSTANCE ?: throw IllegalStateException("ExRateRepository must be initialized")
        }
    }
}