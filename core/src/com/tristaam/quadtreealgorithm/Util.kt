package com.tristaam.quadtreealgorithm

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

object Util {
    fun calculateAngle(xBegin: Float, yBegin: Float, xEnd: Float, yEnd: Float): Float {
        val deltaX = xEnd - xBegin
        val deltaY = yEnd - yBegin
        if (deltaY == 0f) {
            return if (deltaX > 0) PI.toFloat() / 2 else -PI.toFloat() / 2f
        }
        return atan2(deltaY.toDouble(), deltaX.toDouble()).toFloat()
    }

    fun calculateDistance(xBegin: Float, yBegin: Float, xEnd: Float, yEnd: Float): Float {
        val deltaX = xEnd - xBegin
        val deltaY = yEnd - yBegin
        return sqrt(deltaX * deltaX + deltaY * deltaY)
    }
}