package com.example.converter

import com.example.converter.model.ExRateAdjacency


/**
 * Created by Bagdaulet Tleules on 06.12.2021.
 * email: bagdaulettleules@gmail.com
 */
sealed class ExRateViewState {
    object Empty : ExRateViewState()
    data class Loaded(val exRate: ExRateAdjacency) : ExRateViewState()
    data class Error(val message: String) : ExRateViewState()
}