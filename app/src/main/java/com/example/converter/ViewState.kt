package com.example.converter

import com.example.converter.model.Model


/**
 * Created by Bagdaulet Tleules on 06.12.2021.
 * email: bagdaulettleules@gmail.com
 */
sealed class ViewState {
    object Empty : ViewState()
    data class Loaded(val model: Model) : ViewState()
    data class Error(val message: String) : ViewState()
    data class ResultReady(
        val currFrom: String,
        val currTo: String,
        val rate: Double,
        val result: Double?
    ) : ViewState()
}