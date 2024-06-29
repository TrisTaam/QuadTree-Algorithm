package com.tristaam.quadtreealgorithm

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class MainScreen(
    quadTreeAlgorithm: QuadTreeAlgorithm
) : Screen, InputProcessor {
    private val spriteBatch: SpriteBatch = quadTreeAlgorithm.spriteBatch
    private val shapeRenderer: ShapeRenderer = ShapeRenderer()
    private val bitmapFont: BitmapFont = BitmapFont()
    private val balls: MutableList<Ball> = mutableListOf()
    private var xBegin: Float? = null
    private var yBegin: Float? = null
    private var xEnd: Float? = null
    private var yEnd: Float? = null
    private var isHoldRight: Boolean = false
    private var isShowBoundary: Boolean = false
    private var isFilled: Boolean = true
    private lateinit var quadTree: QuadTree

    override fun show() {

    }

    override fun render(deltaTime: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        updateQuadTree()

        renderBalls()
        renderLine()
        if (isShowBoundary) {
            renderQuadTree(quadTree)
        }
        renderHUD()

        updateBalls(deltaTime)
    }

    override fun resize(p0: Int, p1: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        shapeRenderer.dispose()
    }

    override fun keyDown(key: Int): Boolean {
        return false
    }

    override fun keyUp(key: Int): Boolean {
        if (key == Input.Keys.R) {
            createRandomBalls()
            return true
        }
        if (key == Input.Keys.S) {
            isShowBoundary = !isShowBoundary
            return true
        }
        if (key == Input.Keys.C) {
            balls.clear()
            return true
        }
        if (key == Input.Keys.F) {
            isFilled = !isFilled
            return true
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        if (button == Input.Buttons.RIGHT) {
            isHoldRight = true
            return true
        }
        isHoldRight = false
        xBegin = x.toFloat()
        yBegin = Constant.HEIGHT - y.toFloat()
        return true
    }

    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        isHoldRight = false
        if (xBegin == null || yBegin == null || xEnd == null || yEnd == null) {
            return false
        }
        val angle = Util.calculateAngle(xBegin!!, yBegin!!, xEnd!!, yEnd!!) + PI.toFloat()
        val velocity = Util.calculateDistance(
            xBegin!!,
            yBegin!!,
            xEnd!!,
            yEnd!!
        ) / Constant.MAX_LINE_LENGTH * Constant.DEFAULT_VELOCITY
        val ball = Ball(
            position = Vector2(xBegin!!, yBegin!!),
            velocity = Vector2(velocity * cos(angle), velocity * sin(angle))
        )
        balls.add(ball)
        xBegin = null
        yBegin = null
        xEnd = null
        yEnd = null
        return true
    }

    override fun touchCancelled(x: Int, y: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(x: Int, y: Int, pointer: Int): Boolean {
        if (isHoldRight) {
            val ball = Ball(
                position = Vector2(x.toFloat(), Constant.HEIGHT - y.toFloat()),
                radius = Random.nextDouble(Constant.MIN_RADIUS, Constant.MAX_RADIUS).toFloat(),
                velocity = Vector2(
                    Random.nextDouble(-Constant.DEFAULT_VELOCITY.toDouble(), Constant.DEFAULT_VELOCITY.toDouble())
                        .toFloat(),
                    Random.nextDouble(-Constant.DEFAULT_VELOCITY.toDouble(), Constant.DEFAULT_VELOCITY.toDouble())
                        .toFloat()
                ),
            )
            balls.add(ball)
            return true
        }
        if (xBegin == null || yBegin == null) {
            return false
        }
        xEnd = x.toFloat()
        yEnd = Constant.HEIGHT - y.toFloat()
        return false
    }

    override fun mouseMoved(x: Int, y: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return false
    }

    private fun createRandomBalls() {
        for (i in 1..Constant.RANDOM_BALLS_COUNT) {
            val radius = Random.nextDouble(Constant.MIN_RADIUS, Constant.MAX_RADIUS).toFloat()
            val ball = Ball(
                position = Vector2(
                    Random.nextDouble(radius.toDouble(), Constant.WIDTH.toDouble() - radius)
                        .toFloat(),
                    Random.nextDouble(radius.toDouble(), Constant.HEIGHT.toDouble() - radius).toFloat()
                ),
                radius = radius,
                velocity = Vector2(
                    Random.nextDouble(-Constant.DEFAULT_VELOCITY.toDouble(), Constant.DEFAULT_VELOCITY.toDouble())
                        .toFloat(),
                    Random.nextDouble(-Constant.DEFAULT_VELOCITY.toDouble(), Constant.DEFAULT_VELOCITY.toDouble())
                        .toFloat()
                ),
            )
            balls.add(ball)
        }
    }

    private fun renderBalls() {
        spriteBatch.begin()
        shapeRenderer.begin(if (isFilled) ShapeRenderer.ShapeType.Filled else ShapeRenderer.ShapeType.Line)
        for (ball in balls) {
            shapeRenderer.color = ball.color
            shapeRenderer.circle(ball.position.x, ball.position.y, ball.radius)
        }
        shapeRenderer.end()
        spriteBatch.end()
    }

    private fun renderLine() {
        spriteBatch.begin()
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        if (xBegin != null && yBegin != null && xEnd != null && yEnd != null) {
            shapeRenderer.color = Color.GREEN
            shapeRenderer.line(xBegin!!, yBegin!!, xEnd!!, yEnd!!)
        }
        shapeRenderer.end()
        spriteBatch.end()
    }

    private fun renderQuadTree(quadTree: QuadTree) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.color = Color.WHITE
        shapeRenderer.rect(
            quadTree.boundary.position.x,
            quadTree.boundary.position.y,
            quadTree.boundary.size.x,
            quadTree.boundary.size.y
        )
        shapeRenderer.end()
        if (quadTree.isDivided) {
            renderQuadTree(quadTree.northEast!!)
            renderQuadTree(quadTree.northWest!!)
            renderQuadTree(quadTree.southEast!!)
            renderQuadTree(quadTree.southWest!!)
        }
    }

    private fun renderHUD() {
        spriteBatch.begin()
        bitmapFont.draw(
            spriteBatch,
            "FPS: ${Gdx.graphics.framesPerSecond}\nBalls: ${balls.size}\n",
            10f,
            Constant.HEIGHT - 10f
        )
        spriteBatch.end()
    }

    private fun updateBalls(deltaTime: Float) {
        for (ball in balls) {
            ball.update(deltaTime, Constant.WIDTH.toFloat(), Constant.HEIGHT.toFloat())
            collisionDetection(ball)
        }
    }

    private fun updateQuadTree() {
        quadTree = QuadTree(
            boundary = Rectangle(
                position = Vector2(0f, 0f),
                size = Vector2(Constant.WIDTH.toFloat(), Constant.HEIGHT.toFloat())
            ),
            capacity = Constant.CAPACITY
        )
        for (ball in balls) {
            quadTree.insert(ball)
        }
    }

    private fun collisionDetection(ball: Ball) {
        val rectangle = Rectangle(
            position = Vector2(
                ball.position.x - Constant.QUERY_BOUNDARY_SIZE / 2,
                ball.position.y - Constant.QUERY_BOUNDARY_SIZE / 2
            ),
            size = Vector2(Constant.QUERY_BOUNDARY_SIZE, Constant.QUERY_BOUNDARY_SIZE)
        )
        val found = quadTree.query(rectangle)
        for (other in found) {
            if (ball.id == other.id) {
                continue
            }
            if (ball.intersects(other)) {
                ball.collision(other)
            }
        }
    }
}