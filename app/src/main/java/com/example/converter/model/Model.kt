package com.example.converter.model


/**
 * Created by Bagdaulet Tleules on 12.02.2022.
 * email: bagdaulettleules@gmail.com
 */
class Model(
    var currFrom: String = "",
    var currTo: String = "",
    var amount: Double? = null,
    var graph: Graph = Graph()
) {
    val rate get() = initRate()
    val result get() = initResult()

    private fun initRate(): Double {
        if (currFrom.isNotEmpty() && currTo.isNotEmpty()) {
            return graph.getRate(currFrom, currTo)
        }
        return 1.0
    }

    private fun initResult(): Double? {
        if (amount != null) {
            return amount!! * rate
        }
        return null
    }
}