package com.tristaam.quadtreealgorithm

import com.badlogic.gdx.math.Vector2

data class QuadTree(
    val boundary: Rectangle,
    val capacity: Int,
    val balls: MutableList<Ball> = mutableListOf(),
    var northEast: QuadTree? = null,
    var northWest: QuadTree? = null,
    var southEast: QuadTree? = null,
    var southWest: QuadTree? = null,
    var isDivided: Boolean = false
) {
    private fun divide() {
        val x = boundary.position.x
        val y = boundary.position.y
        val w = boundary.size.x
        val h = boundary.size.y
        val ne = Rectangle(Vector2(x + w / 2, y + h / 2), Vector2(w / 2, h / 2))
        val nw = Rectangle(Vector2(x, y + h / 2), Vector2(w / 2, h / 2))
        val se = Rectangle(Vector2(x + w / 2, y), Vector2(w / 2, h / 2))
        val sw = Rectangle(Vector2(x, y), Vector2(w / 2, h / 2))
        northEast = QuadTree(ne, capacity)
        northWest = QuadTree(nw, capacity)
        southEast = QuadTree(se, capacity)
        southWest = QuadTree(sw, capacity)
        isDivided = true
    }

    fun insert(ball: Ball) {
        if (!boundary.contains(ball)) {
            return
        }
        if (balls.size < capacity) {
            balls.add(ball)
            return
        }
        if (!isDivided) {
            divide()
        }
        northEast?.insert(ball)
        northWest?.insert(ball)
        southEast?.insert(ball)
        southWest?.insert(ball)
    }

    fun query(rectangle: Rectangle): List<Ball> {
        val found = mutableListOf<Ball>()
        if (!boundary.intersects(rectangle)) {
            return found
        }
        for (ball in balls) {
            if (rectangle.contains(ball)) {
                found.add(ball)
            }
        }
        if (isDivided) {
            found.addAll(northEast!!.query(rectangle))
            found.addAll(northWest!!.query(rectangle))
            found.addAll(southEast!!.query(rectangle))
            found.addAll(southWest!!.query(rectangle))
        }
        return found
    }
}