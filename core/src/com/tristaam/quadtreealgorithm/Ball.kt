package com.tristaam.quadtreealgorithm

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import kotlin.math.PI
import kotlin.math.sqrt
import kotlin.random.Random

data class Ball(
    val id: Int = idCounter,
    var position: Vector2,
    val radius: Float = Random.nextDouble(Constant.MIN_RADIUS, Constant.MAX_RADIUS).toFloat(),
    var velocity: Vector2 = Vector2(0f, 0f),
    val color: Color = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), Constant.DEFAULT_ALPHA)
) {
    companion object {
        private var idCounter = 0
    }

    init {
        idCounter++
    }

    private val area: Float
        get() = 4f * radius * radius * radius * PI.toFloat() / 3f
    private val mass: Float
        get() = area * Constant.DENSITY

    fun update(deltaTime: Float, width: Float, height: Float) {
        position.x += velocity.x * deltaTime
        position.y += velocity.y * deltaTime
        if (position.x - radius < 0) {
            velocity.x *= -1
            position.x = radius
        } else if (position.x + radius > width) {
            velocity.x *= -1
            position.x = width - radius
        }
        if (position.y - radius < 0) {
            velocity.y *= -1
            position.y = radius
        } else if (position.y + radius > height) {
            velocity.y *= -1
            position.y = height - radius
        }
    }

    private fun distanceTo(other: Ball): Float {
        return sqrt(((position.x - other.position.x) * (position.x - other.position.x) + (position.y - other.position.y) * (position.y - other.position.y)).toDouble()).toFloat()
    }

    fun intersects(other: Ball): Boolean {
        return distanceTo(other) < radius + other.radius
    }

    fun collision(other: Ball) {
        // Interpolate the balls so they don't overlap
        val normal = other.position.cpy().sub(position)
        val distance = position.dst(other.position)
        val overlap = 0.5f * (distance - radius - other.radius)
        position.add(normal.cpy().scl(overlap / distance))
        other.position.sub(normal.cpy().scl(overlap / distance))

        // Calculate the new velocities
        val unitNormal = normal.cpy().nor()
        val unitTangent = Vector2(-unitNormal.y, unitNormal.x)

        val velocityNormal1 = unitNormal.dot(velocity)
        val velocityTangent1 = unitTangent.dot(velocity)
        val velocityNormal2 = unitNormal.dot(other.velocity)
        val velocityTangent2 = unitTangent.dot(other.velocity)

        val velocityNormal1After =
            (velocityNormal1 * (mass - other.mass) + 2 * other.mass * velocityNormal2) / (mass + other.mass)
        val velocityNormal2After =
            (velocityNormal2 * (other.mass - mass) + 2 * mass * velocityNormal1) / (mass + other.mass)

        val velocityNormal1AfterVector = unitNormal.cpy().scl(velocityNormal1After)
        val velocityNormal2AfterVector = unitNormal.cpy().scl(velocityNormal2After)
        val velocityTangent1AfterVector = unitTangent.cpy().scl(velocityTangent1)
        val velocityTangent2AfterVector = unitTangent.cpy().scl(velocityTangent2)

        velocity.x = velocityNormal1AfterVector.x + velocityTangent1AfterVector.x
        velocity.y = velocityNormal1AfterVector.y + velocityTangent1AfterVector.y

        other.velocity.x = velocityNormal2AfterVector.x + velocityTangent2AfterVector.x
        other.velocity.y = velocityNormal2AfterVector.y + velocityTangent2AfterVector.y
    }
}