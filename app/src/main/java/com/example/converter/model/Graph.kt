package com.example.converter.model

import android.util.Log
import java.util.*

/**
 * Created by Bagdaulet Tleules on 06.12.2021.
 * email: bagdaulettleules@gmail.com
 */
class Graph {

    data class Vertex(val name: String) {
        val neighbors = mutableSetOf<Path>()
    }

    private val vertices = hashMapOf<String, Vertex>()

    data class Path(val vertex: Vertex, val rate: Double)

    private operator fun get(name: String) = vertices[name] ?: throw IllegalArgumentException()

    fun addVertex(name: String) {
        vertices[name] = Vertex(name)
    }

    private fun connect(first: Vertex, path: Path) {
        first.neighbors.add(path)
        path.vertex.neighbors.add(Path(first, 1 / path.rate))
    }

    fun connect(first: String, second: String, rate: Double) =
        connect(this[first], Path(this[second], rate))

    fun neighbors(name: String) = vertices[name]?.neighbors ?: emptyList()

    fun getRate(currFrom: String, currTo: String): Double {
        val queue = ArrayDeque<String>()
        queue.add(currFrom)
        val visited = mutableMapOf(currFrom to 1.0)
        while (queue.isNotEmpty()) {
            val next = queue.poll()
            if (next != null) {
                val distance = visited[next]!!
                if (next == currTo) return distance
                for ((vertex, rate) in neighbors(next)) {
                    if (vertex.name in visited) continue
                    visited[vertex.name] = distance * rate
                    queue.add(vertex.name)
                }
            }
        }
        return 1.0
    }

    fun getList() = vertices.map { it.key }

}
