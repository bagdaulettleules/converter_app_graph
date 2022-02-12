package com.example.converter.network

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Bagdaulet Tleules on 05.12.2021.
 * email: bagdaulettleules@gmail.com
 */
interface ExRateApi {
    @GET("latest")
    fun getLatest(@Query("access_key") accessKey: String): Single<ResponseModel>
}