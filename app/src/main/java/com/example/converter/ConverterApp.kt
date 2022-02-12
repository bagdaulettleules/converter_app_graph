package com.example.converter

import android.app.Application
import com.example.converter.network.Repository


/**
 * Created by Bagdaulet Tleules on 05.12.2021.
 * email: bagdaulettleules@gmail.com
 */
class ConverterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.newInstance(this)
    }
}