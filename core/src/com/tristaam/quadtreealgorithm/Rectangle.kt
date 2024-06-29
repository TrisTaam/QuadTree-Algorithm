package com.tristaam.quadtreealgorithm

import com.badlogic.gdx.math.Vector2

data class Rectangle(
    val position: Vector2 = Vector2(0f, 0f),
    val size: Vector2 = Vector2(0f, 0f)
) {
    fun contains(ball: Ball): Boolean {
        return ball.position.x >= position.x &&
                ball.position.x <= position.x + size.x &&
                ball.position.y >= position.y &&
                ball.position.y <= position.y + size.y
    }

    fun intersects(other: Rectangle): Boolean {
        return position.x <= other.position.x + other.size.x &&
                position.x + size.x >= other.position.x &&
                position.y <= other.position.y + other.size.y &&
                position.y + size.y >= other.position.y
    }
}