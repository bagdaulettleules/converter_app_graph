package com.example.converter.database

import androidx.room.Entity
import java.util.*


/**
 * Created by Bagdaulet Tleules on 05.12.2021.
 * email: bagdaulettleules@gmail.com
 */
@Entity(primaryKeys = ["currFrom", "currTo", "date"])
data class ExRateEntity(
    var currFrom: String = "",
    var currTo: String = "",
    var rate: Double = 1.0,
    var date: Date = Date()
)

