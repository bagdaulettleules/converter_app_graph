package com.example.converter.network

import java.io.Serializable
import java.util.*


/**
 * Created by Bagdaulet Tleules on 05.12.2021.
 * email: bagdaulettleules@gmail.com
 */
data class ResponseModel(
    var success: Boolean? = null,
    var timestamp: Int? = null,
    var base: String = "",
    var date: Date = Date(),
    var rates: Map<String, Double>
) : Serializable
